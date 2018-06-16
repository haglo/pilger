package org.app.model.dao;

import java.util.List;

import org.app.model.entity.Settings;

public interface SettingsDAO {

	public Settings update(Settings settings);
	
	public List<Settings> findAll();
}