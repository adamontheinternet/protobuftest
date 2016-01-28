package com.adam.learning;

import com.adam.protos.AddressBookProtos;

/**
 * Created by alaplante on 1/27/16.
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("Running application...");

        AddressBookProtos.Person adam = AddressBookProtos.Person.newBuilder()
            .setId(1234)
            .setName("Adam LaPlante")
            .setEmail("adam@email.com")
            .addPhone(
                AddressBookProtos.Person.PhoneNumber.newBuilder()
                .setNumber("555-1234")
                .setType(AddressBookProtos.Person.PhoneType.MOBILE)
            )
        .build();

        System.out.println("Built AddressBookProtos.Person:\n" + adam);
    }
}
