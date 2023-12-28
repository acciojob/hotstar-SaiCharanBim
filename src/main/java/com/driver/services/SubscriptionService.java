package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.driver.model.SubscriptionType.*;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        SubscriptionType subscriptionType = subscriptionEntryDto.getSubscriptionType();
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionType);
        subscription.setStartSubscriptionDate(new Date());
        Optional<User>user = userRepository.findById(subscriptionEntryDto.getUserId());
        User temp = user.get();
        subscription.setUser(temp);
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        if(subscriptionEntryDto.getSubscriptionType() ==  BASIC)subscription.setTotalAmountPaid(500 + subscriptionEntryDto.getNoOfScreensRequired()*200);
        else if (subscriptionEntryDto.getSubscriptionType() ==  PRO )subscription.setTotalAmountPaid(800 + subscriptionEntryDto.getNoOfScreensRequired()*250);
        else subscription.setTotalAmountPaid(1000 + subscriptionEntryDto.getNoOfScreensRequired()*350);
        subscriptionRepository.save(subscription);
        return subscription.getTotalAmountPaid();
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User>userList = userRepository.findById(userId);
        User temp = userList.get();
        if ( temp.getSubscription().getSubscriptionType() == ELITE) throw new Exception("Already the best Subscription");
        else if ( temp.getSubscription().getSubscriptionType() == BASIC){
            int screens = temp.getSubscription().getNoOfScreensSubscribed();
            int amount = temp.getSubscription().getTotalAmountPaid();
            temp.getSubscription().setSubscriptionType(ELITE);
            temp.getSubscription().setTotalAmountPaid(1000+(350*screens));
            return (1000+(350*screens)-amount);
        }else{
            int screens = temp.getSubscription().getNoOfScreensSubscribed();
            int amount = temp.getSubscription().getTotalAmountPaid();
            temp.getSubscription().setSubscriptionType(ELITE);
            temp.getSubscription().setTotalAmountPaid(1000+(350*screens));
            return (1000+(350*screens)-amount);

        }

    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription>subscriptionList = subscriptionRepository.findAll();
        int revenue = 0 ;
        for( Subscription s : subscriptionList){
            revenue = revenue + s.getTotalAmountPaid();
        }

        return revenue ;
    }

}
