 package ru.nastya.phonebook.model;
 
 import lombok.Data;
 import lombok.NoArgsConstructor;

 import javax.persistence.*;
 
 @Entity
 @Table(name = "contact")
 @Data
 @NoArgsConstructor
 public class Contact {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private String name;
   
   @Column(unique = true)
   private String number;
   
   public Contact(Long id, String name, String number) {
     this.id = id;
     this.name = name;
     this.number = number;
   }
 }
