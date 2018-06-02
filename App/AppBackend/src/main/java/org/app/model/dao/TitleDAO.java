package org.app.model.dao;

import java.util.List;

import org.app.model.entity.Title;

public interface TitleDAO {

	public Title create(Title title);

	public Title update(Title title);

	public void remove(int id);

	public List<Title> findAll();

	public Title findByID(int id);

	public List<Title> findByPriority(int listPrio);

}