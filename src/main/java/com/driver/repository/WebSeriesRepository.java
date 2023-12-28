package com.driver.repository;

import com.driver.model.SubscriptionType;
import com.driver.model.WebSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WebSeriesRepository extends JpaRepository<WebSeries,Integer> {

    WebSeries findBySeriesName(String seriesName);
    @Query( value = "SELECT COUNT(*) FROM WebSeries WHERE SubscriptionType =: subscriptionType and ageLimit = :age OR ageLimit < :age ",nativeQuery = true)
    public int countAccess(int age , SubscriptionType subscriptionType );
    @Query( value = "SELECT * FROM WebSeries WHERE seriesName = :name", nativeQuery = true)
    public  WebSeries isAvailable ( String name );
}
