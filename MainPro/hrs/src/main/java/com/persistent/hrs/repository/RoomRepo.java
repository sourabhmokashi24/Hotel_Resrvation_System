package com.persistent.hrs.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.persistent.hrs.model.Room;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {

	@Query(value = "SELECT * FROM ROOM r WHERE r.is_booked = 0",nativeQuery = true)
	Collection<Room> findAllAvailableRooms();
	
	@Query(value = "SELECT * FROM ROOM r WHERE r.hotel_id = :id",nativeQuery = true )
	Collection<Room> findRoomsByHotelId(@PathVariable("id") Long id);
	
	@Query(value = "SELECT * FROM ROOM r WHERE r.isac = :isAC",nativeQuery = true )
	Collection<Room> findRoomsByAC(@PathVariable("isAC") Boolean isAC);
	
	@Query(value = "SELECT * FROM ROOM r WHERE r.number_of_beds = :numberOfBeds",nativeQuery = true)
	Collection<Room> findRoomsbynumber(@PathVariable("numberOfBeds") int numberOfBeds);
	
}
