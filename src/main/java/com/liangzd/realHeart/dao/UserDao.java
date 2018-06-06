package com.liangzd.realHeart.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer>{

	public Optional<User> findByEmail(String email);
	public Optional<User> findByUsername(String username);
	public Optional<User> findByPhoneNumber(String phoneNumber);
	
	@Query(nativeQuery = true, value="SELECT DISTINCT" + 
			" cast(tu.uid AS CHAR(10)) AS uid,tu.username,tu.gender,tu.email,tu.phone_number" + 
			" , tv.name viprank_name,cast(tu.state as CHAR(10)) as state"+ 
			" ,tu.create_time,tu.nickname,tu.password,tu.id_card,tu.self_introduction" + 
			" FROM" + 
			"	tb_user tu" + 
			" LEFT JOIN tr_user_viprank tuv ON tu.uid = tuv.user_id" + 
			" LEFT JOIN tb_viprank tv ON tv.id = tuv.viprank_id" + 
			" ORDER BY" + 
			"	tu.uid desc")
	public List<Map<String,String>> findAllWithViprankName();
	
	@Query(nativeQuery = true, value="SELECT DISTINCT" + 
			" cast(tu.uid AS CHAR(10)) AS uid,tu.username,tu.gender,tu.email,tu.phone_number" + 
			" , cast(tv. id AS CHAR(10))AS viprank_id,cast(tu.state as CHAR(10)) as state"+ 
			" ,tu.create_time,tu.nickname,tu.password,tu.id_card,tu.self_introduction" + 
			" FROM" + 
			"	tb_user tu" + 
			" LEFT JOIN tr_user_viprank tuv ON tu.uid = tuv.user_id" + 
			" LEFT JOIN tb_viprank tv ON tv.id = tuv.viprank_id" +
			" WHERE tu.uid = :uid" +
			" ORDER BY" + 
			"	tu.uid desc")
	public Map<String,String> findUserByIdWithViprankName(@Param("uid") int uid);
	

	public List<User> findUserByUsernameLikeAndEmailLikeAndPhoneNumberLikeAndGenderLikeAndStateLike(
			String username, String email, String phoneNumber, String gender, Byte State);
	
	public List<User> findUserByUsernameLikeAndEmailLikeAndPhoneNumberLikeAndGenderLike(
			String username, String email, String phoneNumber, String gender);
	
	@Modifying
    @Query("update tb_user tu set tu.password = :password where tu.uid = :uid")
	public void updateUserPassword(@Param("uid")Integer uid, @Param("password")String password);
	
	@Query(nativeQuery=true,value="SELECT * FROM tb_user WHERE gender = :gender AND state = :state AND uid NOT IN (:uids)")
	public Page<User> findByGenderAndStateNotInUids(@Param("gender") String gender,@Param("state") Byte state, 
			@Param("uids") List<Integer> Uid, Pageable pageable);
}
