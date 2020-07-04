##Database:

**blood_bank**
-Id
-Name

**blood_bank_branch**
-Id
-Branch Name
-blood_bank_id
-address_id

**branch_address** 
-Id
-country
-state
-city
-postal_code
-address_line_1
-address_line_2

**member**
-Id
-first_name
-middle_name
-last_name
-blood_group
-date_of_birth
-email

**blood-donor**
-Id
-donor_id(member_id)
-donated_at(blood_bank_branch_id)
-donated_on