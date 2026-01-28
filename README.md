Booking System with Race Condition Protection

OVERVIEW
Build a booking system where multiple users can attempt to book the same slot concurrently.
The system must guarantee that a slot can be booked only once, even under high concurrency.

This assignment tests:
- Understanding of database transactions
- Concurrency and race condition handling
- Database locking strategies
- Spring transactional behavior
- Real-world backend problem solving

CORE PROBLEM
When multiple users try to book the same slot at the same time, the system must prevent
double booking and maintain consistent data.

ROLES
USER
- Can view available slots
- Can book a slot
- Can cancel own booking

2

ADMIN
- Can create slots
- Can cancel any booking

ENTITIES
Slot
- id
- start_time
- end_time
- status (AVAILABLE, BOOKED)

Booking
- id
- slot_id
- user_id
- status (ACTIVE, CANCELLED)
- created_at

SECURITY RULES
- Only authenticated users can book slots
- User can cancel only their own booking
- Admin can cancel any booking
- All actions must be authorized

3

API DETAILS
POST /slots
Role: ADMIN
- Creates a new bookable slot

GET /slots
Role: USER, ADMIN
- Returns list of available and booked slots

POST /bookings
Role: USER
- Attempts to book a slot
- Booking must succeed only if slot is available

POST /bookings/{id}/cancel
Role: USER
- Cancels the user’s own booking

POST /admin/bookings/{id}/cancel
Role: ADMIN
- Cancels any booking

4

CONCURRENCY RULES (MANDATORY)
- Only one booking per slot is allowed
- Concurrent booking requests must not cause double booking
- Must use database-level locking:
- Optimistic locking OR
- Pessimistic locking
- In-memory locks or synchronized blocks are NOT allowed
- Application restart must not affect correctness

TRANSACTION RULES
- Booking operation must be transactional
- Slot status and booking record must always stay consistent
- Partial updates must not occur

DATABASE RULES
- Use H2 database
- Slot status must correctly reflect booking state
- Booking and slot tables must remain consistent

ERROR HANDLING
- Concurrent booking conflicts must be handled gracefully
- User must receive a clear failure response if slot is already booked

5

SUBMISSION RULES
- Upload project to GitHub
- README must explain:
- Chosen locking strategy
- Transaction boundaries
- How race conditions are prevented
- Include example concurrent booking scenario
- Provide steps to run the application
- Unit test case for all ap is required (At least 80% coverage)
- Share a video of yourself at least of 5 mins explaining about project in detail.
- Time Duration to submit is 24 Hours after receiving Email.

NOTE
This is a backend-focused assignment.
No UI is required.
