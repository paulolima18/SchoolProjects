select victim_id, victim_name, race from Usa_Police_Shootings
join dim_race on fk_id_race = id_race;

select mental_ilness, count(victim_id) from Usa_Police_Shootings
join dim_mental_illness on dim_mental_illness_id_mental_illness = id_mental_illness
group by mental_illness;

select victim_name, month from Usa_Police_Shootings
join dim_date on fk_id_date = id_date
join dim_month on fk_id_month = id_month
where month > 3 and month < 7;

select id_city, age from dim_city
join dim_address on fk_id_city = id_city
join Usa_Police_Shootings on fk_id_address = id_address
join dim_age on fk_id_age = id_age
where age > 30;

select victim_id, victim_name, weapon from Usa_Police_Shootings
join dim_weapon on dim_weapon_id_weapon = id_weapon
group by id_weapon;