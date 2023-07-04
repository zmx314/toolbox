package com.zxm.toolbox.dao;

import com.zxm.toolbox.pojo.attn.Position;

import java.util.List;

public interface PositionDao {

	List<Position> findAll();

	Position findById(int id);

	boolean add(Position p);

	boolean delete(Position p);

	int update(Position p);
}
