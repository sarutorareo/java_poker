package com.pecapoker.playingcards;

public class Person {

	private int id;
	private String name;

	public Person(int id, String name)
	{
		this.setId(id);
		this.setName(name);
	}

	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
