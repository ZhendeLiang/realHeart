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

/**
 * 
 * @Description: 用户对象的持久化操作类,tb_user
 * @author liangzd
 * @date 2018年6月16日 下午8:46:08
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer>{

	/**
	 * 
	 * @Description: 根据邮箱查找 用户对象
	 * @param 
	 * @return Optional<User>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:46:40
	 */
	public Optional<User> findByEmail(String email);
	
	/**
	 * 
	 * @Description: 根据用户名查找 用户对象
	 * @param 
	 * @return Optional<User>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:47:00
	 */
	public Optional<User> findByUsername(String username);
	
	/**
	 * 
	 * @Description: 根据手机号查找 用户对象
	 * @param 
	 * @return Optional<User>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:47:21
	 */
	public Optional<User> findByPhoneNumber(String phoneNumber);
	
	/**
	 * 
	 * @Description: 查找所有用户对象(级联抓取用户的会员等级名称)
	 * @param 
	 * @return List<Map<String,String>>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:47:41
	 */
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
	
	/**
	 * 
	 * @Description: 根据用户uid查找 用户对象(级联抓取用户的会员等级id)
	 * @param 
	 * @return Map<String,String>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:48:23
	 */
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
	
	/**
	 * 
	 * @Description: 根据用户名|邮箱|手机号|性别|用户状态进行模糊查找所有 匹配的用户对象
	 * @param 
	 * @return List<User>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:49:47
	 */
	public List<User> findUserByUsernameLikeAndEmailLikeAndPhoneNumberLikeAndGenderLikeAndStateLike(
			String username, String email, String phoneNumber, String gender, Byte State);
	
	/**
	 * 
	 * @Description: 根据用户名|邮箱|手机号|性别进行模糊查找所有 匹配的用户对象
	 * @param 
	 * @return List<User>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:51:23
	 */
	public List<User> findUserByUsernameLikeAndEmailLikeAndPhoneNumberLikeAndGenderLike(
			String username, String email, String phoneNumber, String gender);
	
	/**
	 * 
	 * @Description: 根据用户uid更新用户对象的密码
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月16日 下午8:52:12
	 */
	@Modifying
    @Query("update tb_user tu set tu.password = :password where tu.uid = :uid")
	public void updateUserPassword(@Param("uid")Integer uid, @Param("password")String password);

	/**
	 * 
	 * @Description: 根据性别and用户状态进行分页查找 所有用户对象
	 * @param 
	 * @return Page<User>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:52:43
	 */
	public Page<User> findByGenderAndState(@Param("gender") String gender,@Param("state") Byte state, Pageable pageable);
	
	/**
	 * 
	 * @Description: 根据性别and用户状态和排除在uids中的用户对象进行分页查找 所有匹配的用户对象
	 * @param 
	 * @return Page<User>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:53:41
	 */
	@Query(nativeQuery=true,value="SELECT * FROM tb_user WHERE gender = :gender AND state = :state AND uid NOT IN (:uids)")
	public Page<User> findByGenderAndStateNotInUids(@Param("gender") String gender,@Param("state") Byte state, 
			@Param("uids") List<Integer> Uid, Pageable pageable);
}
