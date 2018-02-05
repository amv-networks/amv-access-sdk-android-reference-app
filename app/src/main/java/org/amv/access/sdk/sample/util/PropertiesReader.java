package org.amv.access.sdk.sample.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public class PropertiesReader {

    private Context context;

    public PropertiesReader(Context context) {
        this.context = requireNonNull(context);
    }

    public Properties getProperties(String fileName) {
        Properties properties = new Properties();
        try {
            AssetManager am = context.getAssets();
            try (InputStream inputStream = am.open(fileName)) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}