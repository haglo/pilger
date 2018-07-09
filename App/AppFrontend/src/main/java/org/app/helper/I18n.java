package org.app.helper;

public final class I18n {

	private static I18n instance;

	final I18nManager i18m = I18nManager.getInstance();

	public static final String LOGIN_VIEW = "Login";
	public static final String PERSON_VIEW = "Person";
	public static final String MASTER_DETAIL_VIEW = "MasterDetail";
	public static final String ACCOUNT_VIEW = "Account";
	public static final String HELP_VIEW = "Help";
	public static final String TITLE_VIEW = "Title";
	public static final String SETTINGS_VIEW = "Settings";
	public static final String SPLIT_VIEW = "Split";
	public static final String THEME = "Theme";
	public static final String LOCALE = "Locale";
	
	public static String MIN_WIDTH;

	public String BASIC_EDIT = i18m.getMessage("basic.edit");
	public String BASIC_SAVE = i18m.getMessage("basic.save");
	public String BASIC_LIST_PRIO = i18m.getMessage("basic.listprio");
	public String BASIC_COMMENT = i18m.getMessage("basic.comment");
	public String BASIC_ABOUT = i18m.getMessage("basic.about");
	public String BASIC_CREATE_BY = i18m.getMessage("basic.createby");
	public String BASIC_CREATE_AT = i18m.getMessage("basic.createat");
	public String BASIC_MODIFY_BY = i18m.getMessage("basic.modifyby");
	public String BASIC_MODIFY_AT = i18m.getMessage("basic.modifyat");
	public String BASIC_EMAIL = i18m.getMessage("basic.email");

	public String AUTH_LOGIN = i18m.getMessage("auth.login");
	public String AUTH_REMEMBER_ME = i18m.getMessage("auth.rememberme");

	public String NAVI_PERSONS = i18m.getMessage("navi.persons");
	public String NAVI_MASTER_DETAIL = i18m.getMessage("navi.masterdetail");
	public String NAVI_ACCOUNTS = i18m.getMessage("navi.accounts");
	public String NAVI_HELP = i18m.getMessage("navi.help");
	public String NAVI_SETTINGS = i18m.getMessage("navi.settings");
	public String NAVI_LOGOUT = i18m.getMessage("navi.logout");

	public String PERSON_SURNAME = i18m.getMessage("person.surname");
	public String PERSON_LASTNAME = i18m.getMessage("person.lastname");
	public String PERSON_STREET = i18m.getMessage("person.street");
	public String PERSON_POSTBOX = i18m.getMessage("person.postbox");
	public String PERSON_ZIPCODE = i18m.getMessage("person.zipcode");
	public String PERSON_CITY = i18m.getMessage("person.city");
	public String PERSON_COUNTRY = i18m.getMessage("person.country");

	public String TITLE_WINDOW_DETAIL_CAPTION = i18m.getMessage("title.windowdetailcaption");
	public String TITLE_WINDOW_NEW_CAPTION = i18m.getMessage("title.windownewcaption");
	public String TITLE_VALUE = i18m.getMessage("title.value");

	public String ACCOUNT_WINDOW_DETAIL_CAPTION = i18m.getMessage("account.windowdetailcaption");
	public String ACCOUNT_WINDOW_NEW_CAPTION = i18m.getMessage("account.windownewcaption");
	public String ACCOUNT_USERNAME = i18m.getMessage("account.username");
	public String ACCOUNT_PASSWORD = i18m.getMessage("account.password");
	public String ACCOUNT_GROUP = i18m.getMessage("account.group");

	public String SETTINGS_LANGUAGE = i18m.getMessage("settings.language");
	public String SETTINGS_THEME = i18m.getMessage("settings.theme");
	public String SETTINGS_WINDOW_WIDTH = i18m.getMessage("settings.windowwidth");

	public String HELP_LANGUAGE = i18m.getMessage("help.language");

	public String NOTIFICATION_NO_ITEM = i18m.getMessage("notification.noItem");
	public String NOTIFICATION_EXACT_ONE_ITEM = i18m.getMessage("notification.oneItem");
	public String NOTIFICATION_ONLY_ONE_ITEM = i18m.getMessage("notification.onlyOneItem");
	public String NOTIFICATION_ONTOP = i18m.getMessage("notification.ontop");
	public String NOTIFICATION_ONTBOTTOM = i18m.getMessage("notification.onBottom");

	public I18n() {
	}

	public static I18n getInstance() {
		if (instance == null) {
			instance = new I18n();
		}
		return instance;
	}

}
