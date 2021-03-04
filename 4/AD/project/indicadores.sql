select aux.id_fact_rating,
aux1.id_place,
aux2.id_user,
r1.rating as rating,
r2.rating as food_rating,
r3.rating as service_rating,
pl1.parking_lot,
co1.latitude as place_latitude,
co1.longitude as place_longitude,
na1.`name`,
ad1.address,
st1.state,
ci1.city,
pr1.budget as place_budget,
am1.ambience as place_ambience,
ac1.accessibility,
ar1.area,
se1.other_services,
al1.alcohol,
sm1.smoking_area,
dr1.dress_code,
sm2.smoker,
di2.drink_level,
de2.dress_preference,
am2.ambience as user_ambience,
tr2.transport,
ki2.kids,
ma2.marital_status,
co2.latitude as user_latitude,
co2.longitude as user_longitude,
ac2.activity,
it2.interest,
ye2.`year`,
pe2.personality,
re2.religion,
he2.height,
we2.weight,
cl2.color,
pr2.budget as user_budget
from Fact_Rating aux
join Dim_Place aux1 on aux1.id_place = aux.id_place
join Dim_User aux2 on aux2.id_user = aux.id_user
left join Dim_Rating r1 on r1.id_rating = aux.id_rating
left join Dim_Rating r2 on r2.id_rating = aux.id_food_rating
left join Dim_Rating r3 on r3.id_rating = aux.id_service_rating
left join Dim_Parking_Lot pl1 on pl1.id_parking_lot = aux1.id_parking_lot
left join Dim_Coordinates co1 on co1.id_coordinates = aux1.id_coordinates
left join Dim_Name na1 on na1.id_name = aux1.id_name
left join Dim_Address ad1 on ad1.id_address = aux1.id_address
left join Dim_State st1 on st1.id_state = aux1.id_state
left join Dim_City ci1 on ci1.id_city = aux1.id_city
left join Dim_Budget pr1 on pr1.id_budget = aux1.id_price
left join Dim_Ambience am1 on am1.id_ambience = aux1.id_ambience
left join Dim_Accessibility ac1 on ac1.id_accessibility = aux1.id_accessibility
left join Dim_Area ar1 on ar1.id_area = aux1.id_area
left join Dim_Services se1 on se1.id_services = aux1.id_services
left join Dim_Alcohol al1 on al1.id_alcohol = aux1.id_alcohol
left join Dim_Smoking_Area sm1 on sm1.id_smoking_area = aux1.id_smoking_area
left join Dim_Dress_Code dr1 on dr1.id_dress_code = aux1.id_dress_code
left join Dim_Smoker sm2 on sm2.id_smoker = aux2.id_smoker
left join Dim_Drink di2 on di2.id_drink = aux2.id_drink
left join Dim_Dress de2 on de2.id_dress = aux2.id_dress
left join Dim_Ambience am2 on am2.id_ambience = aux2.id_ambience
left join Dim_Transport tr2 on tr2.id_transport = aux2.id_transport
left join Dim_Kids ki2 on ki2.id_kids = aux2.id_kids
left join Dim_Marriage ma2 on ma2.id_marriage = aux2.id_marriage
left join Dim_Coordinates co2 on co2.id_coordinates = aux2.id_coordinates
left join Dim_Activity ac2 on ac2.id_activity = aux2.id_activity
left join Dim_Interest it2 on it2.id_interest = aux2.id_interest
left join Dim_Year ye2 on ye2.id_year = aux2.id_year
left join Dim_Personality pe2 on pe2.id_personality = aux2.id_personality
left join Dim_Religion re2 on re2.id_religion = aux2.id_religion
left join Dim_Height he2 on he2.id_height = aux2.id_height
left join Dim_Weight we2 on we2.id_weight = aux2.id_weight
left join Dim_Color cl2 on cl2.id_color = aux2.id_color
left join Dim_Budget pr2 on pr2.id_budget = aux2.id_budget
order by id_fact_rating;