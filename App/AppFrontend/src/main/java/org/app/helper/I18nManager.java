package org.app.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import com.vaadin.ui.UI;

public class I18nManager {

    private final ResourceBundle messages;
    private final Locale locale;

    public I18nManager(Locale locale) {
        this.locale = Objects.requireNonNull(locale);
        messages = ResourceBundle.getBundle("messages", locale, new UnicodeResourceBundleControl());
    }

    public Locale getLocale() {
        return locale;
    }

    public String getMessage(String key, Object... arguments) {
        try {
            final String pattern = messages.getString(key);
            final MessageFormat format = new MessageFormat(pattern, getLocale());
            return format.format(arguments);
        } catch (MissingResourceException ex) {
            return "!" + key;
        }
    }

    private static final ConcurrentHashMap<Locale, I18nManager> MESSAGES_MAP = new ConcurrentHashMap<>();

    public static I18nManager getInstance(Locale locale) {
        if (!MESSAGES_MAP.containsKey(locale)) {
            MESSAGES_MAP.putIfAbsent(locale, new I18nManager(locale));
        }
        return MESSAGES_MAP.get(locale);
    }

    public static I18nManager getInstance() {
        UI current = UI.getCurrent();
        if (current == null) {
            throw new IllegalStateException("No UI bound to current thread");
        }
        return getInstance(current.getLocale());
    }

    private class UnicodeResourceBundleControl extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
            boolean reload) throws IllegalAccessException, InstantiationException, IOException {

            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            final URL resourceURL = loader.getResource(resourceName);
            if (resourceURL == null)
                return null;

            BufferedReader in = new BufferedReader(
                new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8));

            try {
                return new PropertyResourceBundle(in);
            } finally {
                in.close();
            }
        }
    }
}

//public class I18nManager implements Serializable {
//
//	  private static final long serialVersionUID = 1L;
//	  protected ResourceBundle messages;
//	  
//	  public String getMessage(String key) {
//	    if (messages == null) {
//	      createResourceBundle();
//	    }
//	    return messages.getString(key);
//	  }
//
//	  public String getMessage(String key, Object... arguments) {
//	    if (messages == null) {
//	      createResourceBundle();
//	    }
//	    return MessageFormat.format(messages.getString(key), arguments);
//	  }
//	  
//	  public void createResourceBundle() {
//	    Locale locale = ExplorerApp.get().getLocale();
//	    this.messages = ResourceBundle.getBundle(Constants.RESOURCE_BUNDLE, locale);
//	  }
//	  
//	}