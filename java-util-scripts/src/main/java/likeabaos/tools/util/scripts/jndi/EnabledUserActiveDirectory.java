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
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
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
    static final String[] ALPHAS = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
	    "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
    static final String[] DATA_FIELDS = new String[] { "cn", "title", "department", "division", "l", "whenCreated",
	    "info", "lastLogon", "lastLogonTimestamp", "showInAddressBook" };

    static void getActiveDirectoryUsers(String username, String password, String domainController, boolean active)
	    throws IOException {

	String usersContainer = "dc=" + StringUtils.replace(domainController, ".", ",dc=").replace("@", ",dc");
	String ldapFilter = (active)
		? "(&(objectCategory=person)(objectClass=user)(givenName=*)(sn={alpha}*)(manager=*)(!(UserAccountControl:1.2.840.113556.1.4.803:=2)))"
		: "(&(objectCategory=person)(objectClass=user)(givenName=*)(sn={alpha}*)(cn=*,*)(UserAccountControl:1.2.840.113556.1.4.803:=2))";

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
		    extractAttrs(rslt, DATA_FIELDS, data);
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

	if (data.size() > 0) {
	    String filename = (active) ? "active_employees_" : "in-active_employees_";
	    filename = filename + new SimpleDateFormat("yyyyMMdd_HHmmssZ").format(new Date()) + ".csv";
	    File file = new File("output/employees/" + filename);
	    LOG.info("Saving CSV to: {}", file.getAbsolutePath());
	    FileUtils.forceMkdirParent(file);
	    try (CSVPrinter printer = new CSVPrinter(new FileWriter(file),
		    CSVFormat.EXCEL.withHeader(DATA_FIELDS).withQuoteMode(QuoteMode.NON_NUMERIC))) {
		printer.printRecords(data);
	    }
	}
    }

    static void extractAttrs(SearchResult rslt, String[] fields, List<String[]> data) {
	if (rslt == null)
	    return;

	if (fields == null) {
	    printAttrs(rslt.getAttributes());
	    return;
	}

	String[] line = new String[fields.length];
	data.add(line);

	int count = 0;
	for (String field : fields) {
	    String value;
	    try {
		value = getAttributeValue(rslt.getAttributes().get(field));
		if (field.startsWith("lastLogon") && !StringUtils.isBlank(value)) {
		    long fileTime = Long.parseLong(value);
		    Date date = new Date(fileTime / 10000L - 11644473600000L);
		    value = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(date);
		}
	    } catch (Exception e) {
		value = "error";
	    }
	    line[count++] = value;
	}
    }

    static String getAttributeValue(Attribute attr) throws NamingException {
	if (attr == null)
	    return null;

	StringBuilder sb = new StringBuilder();
	int count = 0;
	for (NamingEnumeration<?> e = attr.getAll(); e.hasMore();) {
	    if (++count > 1)
		sb.append("; ");
	    sb.append(String.valueOf(e.next()));
	}
	return sb.toString();
    }

    static void printAttrs(Attributes attrs) {
	if (attrs == null) {
	    System.out.println("No attributes");
	    return;
	}

	try {
	    for (NamingEnumeration<? extends Attribute> ae = attrs.getAll(); ae.hasMore();) {
		Attribute attr = (Attribute) ae.next();
		System.out.println("attribute: " + attr.getID());

		/* print each value */
		for (NamingEnumeration<?> e = attr.getAll(); e.hasMore();) {
		    System.out.println("value: " + e.next());
		}
	    }
	} catch (NamingException e) {
	    e.printStackTrace();
	}
    }
}
