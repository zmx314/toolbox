package com.zxm.toolbox.resources;

public class PropertiesFactory {
	private static final PropertiesFactory instance = new PropertiesFactory();
	private MyProperties SETTINGS_PROPS;
	private MyProperties DUTY_ROSTER_PROPS;
	private MyProperties TEMPLATE_PROPS;
	private MyProperties UI_PROPS;
	private MyProperties VERSION_PROPS;

	private PropertiesFactory() {

	}

	public static PropertiesFactory getInstance() {
		return instance;
	}

	public MyProperties getSettingProps() {
		if (SETTINGS_PROPS == null) {
			SETTINGS_PROPS = new MyProperties(Resources.SETTINGS_PROPS_FILE);
			
		}
		return SETTINGS_PROPS;

	}

	public MyProperties getTemplateProps() {
		if(TEMPLATE_PROPS == null) {
			TEMPLATE_PROPS = new MyProperties(Resources.TEMPLATE_PROPS_FILE);
		}
		return TEMPLATE_PROPS;
	}

	public MyProperties getUiProps() {
		if(UI_PROPS == null) {
			UI_PROPS = new MyProperties(Resources.UI_PROPS_FILE);
		}
		return UI_PROPS;
	}

	public MyProperties getDutyRosterProps() {
		if(DUTY_ROSTER_PROPS == null) {
			DUTY_ROSTER_PROPS = new MyProperties(Resources.DUTY_ROSTER_PROPS);
		}
		return DUTY_ROSTER_PROPS;
	}
}
