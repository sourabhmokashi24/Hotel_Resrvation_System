package com.persistent.hrs.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="room")
public class Room {
	
	@Id
	@GeneratedValue
	private long roomId;
	
	@Column (name="isAC")
	private boolean isAC;
	
	@Column(name="numberOfBeds")
	private int numberOfBeds;
	
	@Column(name="isBooked", columnDefinition = "boolean default false")
	private boolean isBooked;
	
	@Column(name="price")
	private long price;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//	private Hotel hotel;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="hotelId")
	private Hotel hotel;
	
	public Room() {
		
	}
	
	public Room(boolean isAC, int numberOfBeds, boolean isBooked, long price, Hotel hotel) {
		super();
		this.isAC = isAC;
		this.numberOfBeds = numberOfBeds;
		this.isBooked = isBooked;
		this.price = price;
		this.hotel = hotel;
	}
	
	public long getRoomId() {
		return roomId;
	}


	public boolean getIsAC() {
		return isAC;
	}


	public int getNumberOfBeds() {
		return numberOfBeds;
	}


	public boolean getIsBooked() {
		return isBooked;
	}


	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}


	public void setAC(boolean isAC) {
		this.isAC = isAC;
	}


	public void setNumberOfBeds(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}


	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}


	public Hotel getHotel() {
		return hotel;
	}


	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	
}
