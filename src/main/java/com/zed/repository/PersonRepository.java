package com.zed.repository;

import com.zed.model.Person;
import com.zed.model.User;

import java.util.List;

public interface PersonRepository {
    Person save(Person person);
    boolean delete(Person person);
    Person getPersonByKey(Integer personalId);
    Person getPersonByUser(User user);
    List<Person> getAllPersons();
    List<Person> getAllPersonFriends(User user);
    boolean addFriend(Person person, Person anotherPerson);
    boolean dismissFriend(Person person, Person anotherPerson);

}
