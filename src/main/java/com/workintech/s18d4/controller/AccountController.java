package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private AccountService accountService;
    private CustomerService customerService;
    @Autowired
    public AccountController(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @GetMapping
    public List<Account> getAll(){return accountService.findAll();}
    @GetMapping("/{id}")
    public Account get(@PathVariable("id") Long id){
        return accountService.find(id);
    }
    @PostMapping("/{customerId}")
    public AccountResponse save(@PathVariable Long customerId, @RequestBody Account account){
        Customer customer = customerService.find(customerId);
        if(customer != null){
            customer.getAccounts().add(account);
            account.setCustomer(customer);
            accountService.save(account);
        }
        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount());
    }
    @PutMapping("/{customerId}")
    public AccountResponse update(@PathVariable Long customerId, @RequestBody Account account){
        Customer customer = customerService.find(customerId);
        Account aToUpdate = null;
        for(Account a : customer.getAccounts()){
            if(a.getId() == account.getId()){
                aToUpdate = a;
            }
        }
        int indexOfA = customer.getAccounts().indexOf(aToUpdate);
        customer.getAccounts().set(indexOfA, account);
        account.setCustomer(customer);
        accountService.save(account);
        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount());
    }
    @DeleteMapping("/{id}")
    public AccountResponse delete(@PathVariable("id") Long id){
        Account account = accountService.find(id);
        accountService.delete(id);
        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount());
    }

}
