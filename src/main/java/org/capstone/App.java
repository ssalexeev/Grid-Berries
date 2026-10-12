package org.capstone;

import org.capstone.repository.SellerProfileRepository;
import org.capstone.repository.UserRepository;
import org.capstone.service.SellerProfileService;
import org.capstone.service.UserService;

public class App 
{
    public static void main( String[] args )
    {
        UserService userService = new UserService(new UserRepository());
        SellerProfileService profileService = new SellerProfileService(new SellerProfileRepository());




    }
}
