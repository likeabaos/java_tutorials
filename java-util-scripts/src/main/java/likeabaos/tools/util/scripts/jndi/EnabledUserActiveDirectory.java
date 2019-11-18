package likeabaos.tools.util.scripts.jndi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnabledUserActiveDirectory {
    private static final Logger LOG = LogManager.getLogger();
    public static final String[] ALPHAS = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
	    "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

    public static List<String[]> getActiveDirectoryUsers(String username, String password, String domainController,
	    boolean active) throws IOException {

	String usersContainer = "dc=" + StringUtils.replace(domainController, ".", ",dc=").replace("@", ",dc");
	String ldapFilter = (active)
		? "(&(objectCategory=person)(objectClass=user)(givenName=*)(sn={alpha}*)(manager=*)(!(UserAccountControl:1.2.840.113556.1.4.803:=2)))"
		: "(&(objectCategory=person)(objectClass=user)(givenName=*)(sn={alpha}*)(cn=*,*))";

	Properties properties = new Properties();
	properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	properties.put(Context.PROVIDER_URL, "LDAP://" + domainController);
	properties.put(Context.REFERRAL, "follow");
	properties.put(Context.SECURITY_PRINCIPAL, username + "@" + domainController);
	properties.put(Context.SECURITY_CREDENTIALS, password);

	// initializing active directory LDAP connection
	List<String[]> data = new ArrayList<>();
	DirContext dirContext = null;
	try {
	    dirContext = new InitialDirContext(properties);
	    LOG.info("Connected!");

	    SearchControls ctls = new SearchControls();
	    ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

	    boolean hasLimit = false;
	    for (String alpha : ALPHAS) {
		String filter = StringUtils.replace(ldapFilter, "{alpha}", alpha);
		NamingEnumeration<?> answer = dirContext.search(usersContainer, filter, ctls);
		LOG.info("Querying for {}* result...", alpha);
		int count = 0;
		while (true) {
		    boolean hasMore = false;
		    try {
			hasMore = answer.hasMore();
		    } catch (NamingException e) {
			LOG.error("Error getting element #{} because: {}", count + 1, e.getMessage());
		    }
		    if (!hasMore)
			break;
		    count++;
		    SearchResult rslt = (SearchResult) answer.next();
		    QueryActiveDirectory.extractAttrs(rslt, QueryActiveDirectory.DEFAULT_DATA_FIELDS, data);
		    if (hasLimit && count >= 10)
			break;
		}
	    }

	} catch (Exception e) {
	    LOG.fatal("Unknown error, cannot continue.", e);
	} finally {
	    if (dirContext != null)
		try {
		    dirContext.close();
		    LOG.info("Closed connection");
		} catch (Exception e) {
		    LOG.error("Unable to close connection", e);
		}
	}

	return data;
    }

    public static void exportCsv(String username, String password, String domainController, boolean active)
	    throws IOException {
	List<String[]> data = getActiveDirectoryUsers(username, password, domainController, active);
	if (data.size() > 0) {
	    String filename = (active) ? "active_employees" : "in-active_employees";
	    exportCsv(data, filename);
	} else {
	    LOG.warn("No data returned from query");
	}
    }

    public static void exportCsv(List<String[]> data, String filename) throws IOException {
	String fullFilename = filename + new SimpleDateFormat("_yyyyMMdd_HHmmssZ").format(new Date()) + ".csv";
	File file = new File("output/employees/" + fullFilename);
	LOG.info("Saving CSV to: {}", file.getAbsolutePath());
	FileUtils.forceMkdirParent(file);
	try (CSVPrinter printer = new CSVPrinter(new FileWriter(file), CSVFormat.EXCEL
		.withHeader(QueryActiveDirectory.DEFAULT_DATA_FIELDS).withQuoteMode(QuoteMode.NON_NUMERIC))) {
	    printer.printRecords(data);
	}
    }
}
