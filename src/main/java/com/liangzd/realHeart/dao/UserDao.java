package com.liangzd.realHeart.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer>{

	public Optional<User> findByEmail(String email);
	public Optional<User> findByUsername(String username);
	public Optional<User> findByPhoneNumber(String phoneNumber);
	
	@Query(nativeQuery = true, value="SELECT" + 
			" tu.uid,tu.username,tu.gender,tu.email,tu.phone_number" + 
			" , tv.name viprank_name,cast(tu.state as char) as state"+ 
			" ,tu.create_time,tu.nickname,tu.password,tu.id_card,tu.self_introduction" + 
			" FROM" + 
			"	tb_user tu" + 
			" LEFT JOIN tr_user_viprank tuv ON tu.uid = tuv.user_id" + 
			" LEFT JOIN tb_viprank tv ON tv.id = tuv.viprank_id" + 
			" ORDER BY" + 
			"	tu.uid desc")
	public List<Map<String,String>> findAllWithViprankName();
	
}
