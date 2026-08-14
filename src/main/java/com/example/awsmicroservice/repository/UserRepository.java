package com.example.awsmicroservice.repository;

import com.example.awsmicroservice.entity.User;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepository {

    private final DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbTable<User> userTable) {
        this.userTable = userTable;
    }

    public void save(User user) {
        userTable.putItem(user);
    }

    public User findById(String userId) {
        Key key = Key.builder().partitionValue(userId).build();
        return userTable.getItem(key);
    }

    public List<User> findAll() {
        return userTable.scan().items().stream().collect(Collectors.toList());
    }

    public void delete(String userId) {
        Key key = Key.builder().partitionValue(userId).build();
        userTable.deleteItem(key);
    }
}
