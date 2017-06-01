package com.gstar.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gstar.security.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	public Account findByUserId(String userId);
	
	@Query("select account from Account account where account.userId = ?#{principal.Username}")
	public Account findMe();
	
	public Account findByAuthTypeAndAuthId(String authType, String authId);
	
}
