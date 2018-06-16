package com.liangzd.realHeart.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * @Description: tb_viprank表的持久化类,会员详细信息表
 * @author liangzd
 * @date 2018年6月16日 下午9:28:11
 */
@Entity(name="tb_viprank")
public class TbViprank implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;//主键
	@Column(length=10)
	private String name;//会员等级名称
	@Column(length=6)
	private BigDecimal price;//会员等级对应价格
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
