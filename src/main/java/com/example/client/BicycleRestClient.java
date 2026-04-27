package com.example.client;

import com.example.entity.Bicycle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class BicycleRestClient {

    private static final String BASE_URL = "http://localhost:8080/api/bicycles";
    private final RestTemplate restTemplate;

    public BicycleRestClient() {
        this.restTemplate = new RestTemplate();
    }

    // GET все велосипеды
    public List<Bicycle> getAllBicycles() {
        ResponseEntity<Bicycle[]> response = restTemplate.getForEntity(BASE_URL, Bicycle[].class);
        return Arrays.asList(response.getBody());
    }

    // GET велосипед по ID
    public Bicycle getBicycleById(Long id) {
        return restTemplate.getForObject(BASE_URL + "/" + id, Bicycle.class);
    }

    // POST добавить велосипед
    public Bicycle createBicycle(Bicycle bicycle) {
        return restTemplate.postForObject(BASE_URL, bicycle, Bicycle.class);
    }

    // PUT обновить велосипед
    public void updateBicycle(Long id, Bicycle bicycle) {
        restTemplate.put(BASE_URL + "/" + id, bicycle);
    }

    // DELETE удалить велосипед
    public void deleteBicycle(Long id) {
        restTemplate.delete(BASE_URL + "/" + id);
    }

    // GET поиск по цене
    public List<Bicycle> findBicyclesByCost(Integer cost) {
        ResponseEntity<Bicycle[]> response = restTemplate.getForEntity(
                BASE_URL + "/search?cost=" + cost, Bicycle[].class);
        return Arrays.asList(response.getBody());
    }

    // Тестовый метод
    public static void main(String[] args) {
        BicycleRestClient client = new BicycleRestClient();

        System.out.println("=== ТЕСТ REST CLIENT ===");

        try {
            // 1. Получить все велосипеды
            System.out.println("1. Все велосипеды:");
            List<Bicycle> bicycles = client.getAllBicycles();
            bicycles.forEach(System.out::println);

            // 2. Добавить новый велосипед
            System.out.println("\n2. Добавляем новый велосипед:");
            Bicycle newBicycle = new Bicycle();
            newBicycle.setModel("Test Model");
            newBicycle.setProducer("Test Producer");
            newBicycle.setProducingCountry("Test Country");
            newBicycle.setGearsNum(21);
            newBicycle.setCost(999);

            Bicycle createdBicycle = client.createBicycle(newBicycle);
            System.out.println("Добавлен: " + createdBicycle);

            // 3. Получить велосипед по ID
            System.out.println("\n3. Ищем добавленный велосипед:");
            Bicycle foundBicycle = client.getBicycleById(createdBicycle.getId());
            System.out.println("Найден: " + foundBicycle);

            // 4. Обновить велосипед
            System.out.println("\n4. Обновляем велосипед:");
            foundBicycle.setCost(1299);
            client.updateBicycle(foundBicycle.getId(), foundBicycle);
            System.out.println("Обновлен: " + client.getBicycleById(foundBicycle.getId()));

            // 5. Поиск по цене
            System.out.println("\n5. Поиск велосипедов дороже 1000:");
            List<Bicycle> expensiveBicycles = client.findBicyclesByCost(1000);
            expensiveBicycles.forEach(System.out::println);

            // 6. Удалить велосипед
            System.out.println("\n6. Удаляем тестовый велосипед:");
            client.deleteBicycle(createdBicycle.getId());
            System.out.println("Велосипед удален");

            // 7. Проверяем что удалился
            System.out.println("\n7. Проверяем удаление:");
            bicycles = client.getAllBicycles();
            System.out.println("Осталось велосипедов: " + bicycles.size());

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            System.out.println("Убедитесь, что сервер запущен на http://localhost:8080");
        }
    }
}