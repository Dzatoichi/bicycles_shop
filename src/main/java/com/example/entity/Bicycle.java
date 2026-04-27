/**
 * Класс реализуемой сущности
 Constructs a Bicycle object with the specified parameters.
 @param id индификатор.
 @param model имя модеели.
 @param producer имя бренда производителя.
 @param producingCountry имя страны производителя.
 @param gearsNum число скоростей.
 @param cost цена.
 */

package com.example.entity;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Component("BicycleBean")
public class Bicycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;
    private String producer;
    private String producingCountry;
    private Integer gearsNum;
    private Integer cost;
    public Bicycle(String model, String producer, String producingCountry, Integer gearsNum, Integer cost){
        this.model = model;
        this.producer = producer;
        this.producingCountry = producingCountry;
        this.gearsNum = gearsNum;
        this.cost = cost;
    }
    public Bicycle(){
        this.model = "Kawasaki";
        this.producer = "NIKE";
        this.producingCountry = "Russia";
        this.gearsNum = 21;
        this.cost = 5000;
    }
    public String toString(){
        return "\nid: " + getId() +
                "\nmodel: " + getModel() +
                "\nproducer: " + getProducer() +
                "\nproducingCountry: " + getProducingCountry() +
                "\ngearsNum: "+ getGearsNum() +
                "\ncost: " + getCost() + "\n";
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getProducingCountry() {
        return producingCountry;
    }

    public void setProducingCountry(String producingCountry) {
        this.producingCountry = producingCountry;
    }

    public Integer getGearsNum() {
        return gearsNum;
    }

    public void setGearsNum(Integer gears_num) {
        this.gearsNum = gears_num;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
