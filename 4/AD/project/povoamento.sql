-- ====================== PARTE 1 (Imports) ====================== (OK)

use restaurants;
select * from Aux_User;

-- import geoplaces --> aux_place
-- import userprofiles --> aux_user
-- import ratings --> aux_rating
-- import parking --> aux_parking

-- ====================== PARTE 2 (Inserts) ====================== (OK)

-- TIPOS FIXOS NO README

insert into Dim_Accessibility (accessibility) values ('no_accessibility'),
													 ('completely'),
													 ('partially');

insert into Dim_Activity (activity) values ('student'),
                                           ('professional'),
                                           ('unemployed'),
                                           ('working-class');

insert into Dim_Alcohol (alcohol) values ('No_Alcohol_Served'),
                                         ('Wine-Beer'),
                                         ('Full_Bar');

insert into Dim_Ambience (ambience) values ('familiar'),
                                           ('quiet');

insert into Dim_Ambience (ambience) values ('family'),
                                           ('friends'),
                                           ('solitary');

insert into Dim_Area (area) values ('open'),
                                   ('closed');

insert into Dim_Budget (budget) values ('medium'),
                                       ('low'),
                                       ('high');

insert into Dim_Color (color) values ('black'),
                                     ('red'),
                                     ('blue'),
                                     ('green'),
                                     ('purple'),
                                     ('orange'),
                                     ('yellow'),
                                     ('white');

insert into Dim_Dress (dress_preference) values ('informal'),
                                                ('formal'),
                                                ('no preference'),
                                                ('elegant');

insert into Dim_Dress_Code (dress_code) values ('informal'),
                                               ('casual'),
                                               ('formal');

insert into Dim_Drink (drink_level) values ('abstemious'),
                                           ('social drinker'),
                                           ('casual drinker');

insert into Dim_Interest (interest) values ('variety'),
                                           ('technology'),
                                           ('none'),
                                           ('retro'),
                                           ('eco-friendly');

insert into Dim_Kids (kids) values ('independent'),
                                   ('kids'),
                                   ('dependent');

insert into Dim_Marriage (marital_status) values ('single'),
                                                 ('married'),
                                                 ('widow');

insert into Dim_Parking_Lot (parking_lot) values ('public'),
                                                 ('none'),
                                                 ('yes'),
                                                 ('valet parking'),
                                                 ('free'),
                                                 ('street'),
                                                 ('validated_parking');

insert into Dim_Personality (personality) values ('thrifty-protector'),
                                                 ('hunter-ostentatious'),
                                                 ('hard-worker'),
                                                 ('conformist');

insert into Dim_Rating (rating) values (0),
                                       (1),
                                       (2);

insert into Dim_Religion (religion) values ('none'),
                                           ('Catholic'),
                                           ('Christian'),
                                           ('Mormon'),
                                           ('Jewish');

insert into Dim_Services (other_services) values ('none'),
                                                 ('internet'),
                                                 ('variety');

insert into Dim_Smoker (smoker) values ('false'),
                                       ('true');

insert into Dim_Smoking_Area (smoking_area) values ('none'),
                                                   ('only at bar'),
                                                   ('permitted'),
                                                   ('section'),
                                                   ('not permitted');

insert into Dim_Transport (transport) values ('on foot'),
                                             ('public'),
                                             ('car owner');

-- TIPOS EXTRA

insert into Dim_Address (address)
select distinct address from Aux_Place;

insert into Dim_City (city)
select distinct city from Aux_Place;

insert into Dim_Coordinates (latitude, longitude)
select distinct latitude, longitude
from Aux_Place;

insert into Dim_Coordinates (latitude, longitude)
select distinct latitude, longitude from Aux_User
where (latitude,longitude) not in (select latitude,longitude from Dim_Coordinates);

insert into Dim_Height (height)
select distinct height from Aux_User;

insert into Dim_Name (`name`)
select distinct `name` from Aux_Place;

insert into Dim_State (state, country)
select distinct state, country from Aux_Place;

insert into Dim_Weight (weight)
select distinct weight from Aux_User;

insert into Dim_Year (`year`)
select distinct `year` from Aux_User;

-- ====================== PARTE 3 (Inserts Complexos) ====================== (OK)

insert into Dim_Place (id_place, id_parking_lot, id_coordinates, id_name, id_address, id_state, id_city, id_price, id_ambience, id_accessibility, id_area, id_services, id_alcohol, id_smoking_area, id_dress_code)
select aux.id_place,
pl.id_parking_lot,
co.id_coordinates,
na.id_name,
ad.id_address,
st.id_state,
ci.id_city,
pr.id_budget,
am.id_ambience,
ac.id_accessibility,
ar.id_area,
se.id_services,
al.id_alcohol,
sm.id_smoking_area,
dr.id_dress_code
from Aux_Place aux
left join Aux_Parking aux_pl on aux_pl.id_place = aux.id_place
left join Dim_Parking_Lot pl on pl.parking_lot = aux_pl.parking
left join Dim_Coordinates co on co.latitude = aux.latitude and co.longitude = aux.longitude
left join Dim_Name na on na.`name` = aux.`name`
left join Dim_Address ad on ad.address = aux.address
left join Dim_State st on st.state = aux.state and st.country = aux.country
left join Dim_City ci on ci.city = aux.city
left join Dim_Budget pr on pr.budget = aux.price
left join Dim_Ambience am on am.ambience = aux.ambience
left join Dim_Accessibility ac on ac.accessibility = aux.accessibility
left join Dim_Area ar on ar.area = aux.area
left join Dim_Services se on se.other_services = aux.services
left join Dim_Alcohol al on al.alcohol = aux.alcohol
left join Dim_Smoking_Area sm on sm.smoking_area = aux.smoking_area
left join Dim_Dress_Code dr on dr.dress_code = aux.dress_code;

-- apaga users com demasiados nulls
delete from Aux_User where id_user = 'U1024' or id_user = 'U1083' or id_user = 'U1122' or id_user = 'U1130';

insert into Dim_Smoker (smoker) values ('X_NONE_X');
insert into Dim_Drink (drink_level) values ('X_NONE_X');
insert into Dim_Dress (dress_preference) values ('X_NONE_X');
insert into Dim_Ambience (ambience) values ('X_NONE_X');
insert into Dim_Transport (transport) values ('X_NONE_X');
insert into Dim_Kids (kids) values ('X_NONE_X');
insert into Dim_Marriage (marital_status) values ('X_NONE_X');
insert into Dim_Activity (activity) values ('X_NONE_X');
insert into Dim_Interest (interest) values ('X_NONE_X');
insert into Dim_Personality (personality) values ('X_NONE_X');
insert into Dim_Religion (religion) values ('X_NONE_X');
insert into Dim_Color (color) values ('X_NONE_X');
insert into Dim_Budget (budget) values ('X_NONE_X');

insert into Dim_User (id_user, id_smoker, id_drink, id_dress, id_ambience, id_transport, id_kids, id_marriage, id_coordinates, id_activity, id_interest, id_year, id_personality, id_religion, id_height, id_weight, id_color, id_budget)
select aux.id_user,
sm.id_smoker,
di.id_drink,
de.id_dress,
am.id_ambience,
tr.id_transport,
ki.id_kids,
ma.id_marriage,
co.id_coordinates,
ac.id_activity,
it.id_interest,
ye.id_year,
pe.id_personality,
re.id_religion,
he.id_height,
we.id_weight,
cl.id_color,
pr.id_budget
from Aux_User aux
left join Dim_Smoker sm on sm.smoker = aux.smoker
left join Dim_Drink di on di.drink_level = aux.drink
left join Dim_Dress de on de.dress_preference = aux.dress
left join Dim_Ambience am on am.ambience = aux.ambience
left join Dim_Transport tr on tr.transport = aux.transport
left join Dim_Kids ki on ki.kids = aux.kids
left join Dim_Marriage ma on ma.marital_status = aux.marriage
left join Dim_Coordinates co on co.latitude = aux.latitude and co.longitude = aux.longitude
left join Dim_Activity ac on ac.activity = aux.activity
left join Dim_Interest it on it.interest = aux.interest
left join Dim_Year ye on ye.`year` = aux.`year`
left join Dim_Personality pe on pe.personality = aux.personality
left join Dim_Religion re on re.religion = aux.religion
left join Dim_Height he on he.height = aux.height
left join Dim_Weight we on we.weight = aux.weight
left join Dim_Color cl on cl.color = aux.color
left join Dim_Budget pr on pr.budget = aux.budget;

-- ====================== PARTE 4 (Tabela de Facto) ====================== (OK)

insert into Fact_Rating (id_user, id_place, id_rating, id_food_rating, id_service_rating)
select u.id_user,
p.id_place,
r1.id_rating,
r2.id_rating,
r3.id_rating
from Aux_Rating aux
join Dim_User u on u.id_user = aux.id_user
join Dim_Place p on p.id_place = aux.id_place
left join Dim_Rating r1 on r1.rating = aux.rating
left join Dim_Rating r2 on r2.rating = aux.food_rating
left join Dim_Rating r3 on r3.rating = aux.service_rating;

select * from Fact_Rating;