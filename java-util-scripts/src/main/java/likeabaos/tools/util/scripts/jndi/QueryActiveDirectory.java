package likeabaos.tools.util.scripts.jndi;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import likeabaos.tools.util.scripts.gui.SimpleDataTable;

public class QueryActiveDirectory {
    private static final Logger LOG = LogManager.getLogger();

    private String username;
    private String password;
    private String domainController;
    private String referralPolicy = "follow";
    private String[] dataFields;
    private List<String[]> result;

    public QueryActiveDirectory(String username, String password, String domainController) {
	this.username = username;
	this.password = password;
	this.domainController = domainController;
	this.dataFields = DEFAULT_DATA_FIELDS;
    }

    public String[] getDataFields() {
	return dataFields;
    }

    public void setDataFields(String[] dataFields) {
	this.dataFields = dataFields;
    }

    public void setReferralPolicy(String policy) {
	this.referralPolicy = policy;
    }

    public String getReferralPolicy() {
	return referralPolicy;
    }

    public static String buildUserFilter(String search) {
	return QueryActiveDirectory.buildFilters("&", "objectCategory=person", "objectClass=user", "cn=" + search);
    }

    public static String buildFilters(String operator, String... filters) {
	StringBuilder sb = new StringBuilder();
	sb.append("(").append(operator);
	for (String f : filters) {
	    sb.append("(").append(f).append(")");
	}
	sb.append(")");
	return sb.toString();
    }

    public boolean query(String ldapFilter, Integer max) throws IOException {
	String usersContainer = "dc=" + StringUtils.replace(this.domainController, ".", ",dc=").replace("@", ",dc");
	Properties properties = new Properties();
	properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	properties.put(Context.PROVIDER_URL, "LDAP://" + this.domainController);
	properties.put(Context.REFERRAL, this.getReferralPolicy());
	properties.put(Context.SECURITY_PRINCIPAL, username + "@" + this.domainController);
	properties.put(Context.SECURITY_CREDENTIALS, password);

	List<String[]> data = new ArrayList<>();
	DirContext dirContext = null;
	boolean complete = false;
	try {
	    dirContext = new InitialDirContext(properties);
	    LOG.info("Connected!");

	    SearchControls ctls = new SearchControls();
	    ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

	    NamingEnumeration<?> answer = dirContext.search(usersContainer, ldapFilter, ctls);
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
		extractAttrs(rslt, this.getDataFields(), data);
		if (max != null && count >= max) {
		    LOG.info("Retrieved {} entries, there could be more...");
		    break;
		}
	    }
	    complete = true;
	    this.result = data;
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

	return complete;
    }

    public List<String[]> getResult() {
	return result;
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

    public void showAsJTable(String title) {
	List<String[]> data = this.getResult();
	Object[][] tableData = new Object[data.size()][this.getDataFields().length];
	for (int i = 0; i < data.size(); i++) {
	    for (int j = 0; j < this.getDataFields().length; j++) {
		tableData[i][j] = data.get(i)[j];
	    }
	}
	title = (title == null) ? this.getClass().getName() : title;
	SimpleDataTable sdt = new SimpleDataTable();
	sdt.show(title, tableData, this.getDataFields());
    }

    public static final String[] DEFAULT_DATA_FIELDS = new String[] { "cn", "title", "department", "division", "l",
	    "whenCreated", "info", "lastLogon", "lastLogonTimestamp", "showInAddressBook" };
}
