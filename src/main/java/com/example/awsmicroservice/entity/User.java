package com.example.awsmicroservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class User {
    
    private String userId;
    private String name;

    // AWS DynamoDB specifically requires the partition key annotation 
    // to be placed on the getter method, not on the variable!
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
}
