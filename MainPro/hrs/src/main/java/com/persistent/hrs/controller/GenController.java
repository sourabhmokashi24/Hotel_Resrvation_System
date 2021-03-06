package com.persistent.hrs.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.persistent.hrs.exception.ResourceNotFoundException;
import com.persistent.hrs.model.User;
import com.persistent.hrs.model.Booking;
import com.persistent.hrs.model.Hotel;
import com.persistent.hrs.model.Room;
import com.persistent.hrs.repository.BookingRepo;
import com.persistent.hrs.repository.HotelRepo;
import com.persistent.hrs.repository.Repo;
import com.persistent.hrs.repository.RoomRepo;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1/")
public class GenController {
	
	@Autowired
	private Repo repo;
	@Autowired
	private HotelRepo hotelrepo;
	@Autowired
	private RoomRepo roomrepo;
	@Autowired
	private BookingRepo bookingrepo;
	
	@GetMapping("/users")
	public List<User> getAllUser() {
		return repo.findAll();
	}
	
	@GetMapping("/users/{emailid}")
	public ResponseEntity<User> getUser(@PathVariable("emailid") String emailid) {
		User user = repo.findById(emailid).orElseThrow(() -> new ResourceNotFoundException("User Not Exist"));
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/users")
	public User addUser(@RequestBody User user) {
		return repo.save(user);
	}
	
	@PutMapping("/users/{emailid}")
	public ResponseEntity<User> updateUser(@PathVariable String emailid, @RequestBody User userDetails) {
		Optional<User> userEntity = repo.findById(emailid);
		User user = userEntity.get();
		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setAadhar(userDetails.getAadhar());
		user.setPhoneNumber(userDetails.getPhoneNumber());
		user.setPassword(userDetails.getPassword());
		User updatedUser = repo.save(user);
		return ResponseEntity.ok(updatedUser);
	}
	
	
	/* Hotels Apis */
	
	@GetMapping("/hotels")
	public List<Hotel> getAllHotels() {
		return hotelrepo.findAll();
	}
	
	@PostMapping("/owner")
	public Hotel addOwner(@RequestBody Hotel hotel) {
		return hotelrepo.save(hotel);
	}
	
	@GetMapping("/owner/{emailid}")
	public Hotel getOwnerbyId(@PathVariable("emailid") String emailid) {
		Hotel hotel = hotelrepo.findByemailId(emailid);
		return hotel;
	}
	
	@PutMapping("/updateHotelDetails/{id}")
	public ResponseEntity<Hotel> updateEmployee(@PathVariable("id") Long id, @RequestBody Hotel hotelDetails) {
		Optional<Hotel> optinalEntity =  hotelrepo.findById(id);
		 Hotel hotelObj = optinalEntity.get();
		 hotelObj.setHotelName(hotelDetails.getHotelName());
		 hotelObj.setOwnerName(hotelDetails.getOwnerName());
		 hotelObj.setAddress(hotelDetails.getAddress());
		 hotelObj.setCity(hotelDetails.getCity());
		 hotelObj.setPassword(hotelDetails.getPassword());
		 hotelObj.setEmailId(hotelDetails.getEmailId());
		 hotelObj.setPhoneNumber(hotelDetails.getPhoneNumber());
		Hotel updatedHotel = hotelrepo.save(hotelObj);
		return ResponseEntity.ok(updatedHotel);
	}
	
	/*
	@GetMapping("/login")
	public String login (@RequestBody Hotel obj){
		System.out.println(obj);
		List<Hotel> hotelObj = hotelrepo.findByemailId(obj.getEmailId());
		if(!hotelObj.isEmpty() && hotelObj.get(0).getPassword().equals(obj.getPassword())) {
			return "Login Successfull";
		}else {
			return "Please Login again!";
		}
	}
	*/
	
	
	/* Room Apis */
	
	@PostMapping("/addRooms")
	public ResponseEntity<Room> addRoom(@RequestBody Room room){
		Optional<Hotel> optionalHotel = hotelrepo.findById(room.getHotel().getHotelId());
        if (!optionalHotel.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        room.setHotel(optionalHotel.get());

        Room savedRoom = roomrepo.save(room);
        
        return ResponseEntity.ok(savedRoom);
	}
	
	@GetMapping("/rooms")
	public List<Room> getAllRooms() {
		return roomrepo.findAll();
	}
	
	@GetMapping("/getRoom/{id}")
	public ResponseEntity<Room> getRoom (@PathVariable("id") Long id){
		return ResponseEntity.ok(roomrepo.findById(id).get());
	}
	
	@PutMapping("/updateRoom/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable("id") Long id,@RequestBody Room room) {

        Optional<Room> optionalRoom = roomrepo.findById(id);
        if (!optionalRoom.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        Room roomObj = optionalRoom.get();
        roomObj.setAC(room.getIsAC());
        roomObj.setNumberOfBeds(room.getNumberOfBeds());
        roomObj.setPrice(room.getPrice());
        return ResponseEntity.ok(roomrepo.save(roomObj));
    }
	
	//Get rooms by HotelId
	@GetMapping("/showRooms/{id}")
	public Collection<Room> getRoomsByHotelId(@PathVariable("id") Long id){
		return roomrepo.findRoomsByHotelId(id);
	}
	
	
	@GetMapping(("/getAvailableRooms"))
	public Collection<Room> getAvailableRooms(){
		return roomrepo.findAllAvailableRooms();
	}
	
	@GetMapping("/getRoomsByAc/{isAC}")
	public Collection<Room> getRoomsByAc(@PathVariable("isAC") Boolean isAC){
		return roomrepo.findRoomsByAC(isAC);
	}
	
	@GetMapping("/getRoombybednumber/{numberOfBeds}")
	public Collection<Room> getroombybednumber(@PathVariable("numberOfBeds") int numberOfBeds) {
		return roomrepo.findRoomsbynumber(numberOfBeds);
	}
	
	/* Booking Api's */
	
	@PostMapping("/bookRoom")
	public ResponseEntity<Booking> bookRoom (@RequestBody Booking booking){
		Optional<Room> optionalRoomObj = roomrepo.findById(booking.getRoom().getRoomId());
        if (!optionalRoomObj.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        Optional<User> optionalUserObj = repo.findById((booking.getUser().getEmailId()));
        if (!optionalRoomObj.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        optionalRoomObj.get().setBooked(true);
        booking.setRoom(optionalRoomObj.get());
        booking.setUser(optionalUserObj.get());

        Booking savedBooking = bookingrepo.save(booking);
        
        return ResponseEntity.ok(savedBooking);
	}
	
	
	/* Booking Api's */
	
	/*
	public ResponseEntity<Booking> getBooking(@RequestBody Booking booking){
		Booking bookingObj = bookingrepo.getById(booking.getBookingId());
		return ResponseEntity.ok(bookingObj);
	}
	*/
	
	@GetMapping("/getBooking/{bookingId}")
	public Optional<Booking> getBookingById(@PathVariable("bookingId") Long bookingId){
		Optional<Booking> bookingObj = bookingrepo.findById(bookingId);
		return bookingObj;
	}
	
	@GetMapping("/getAllBookings/{emailId}")
	public Collection<Booking> getAllBookings(@PathVariable("emailId") String emailId){
		return bookingrepo.findBookings(emailId);
	}
	
	//Get Bookings by Hotel Id
		@GetMapping("/getBokingsForOwner/{id}")
		public List<Booking> getBookingsByHotelId(@PathVariable("id") Long id){
			
			Collection<Room> rooms = roomrepo.findRoomsByHotelId(id);
			List<Booking> bookings = new ArrayList<Booking>();
			bookings = rooms.stream()
					.map(r->bookingrepo.findBoookingsByRoomId(r.getRoomId()))
					.collect(Collectors.toList());
			while(bookings.remove(null));
			return bookings;
		}
		

}
