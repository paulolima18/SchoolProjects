use MusicDB;

delete from User;
delete from Music_has_Genre;
delete from Music;
delete from Genre;

#Povoamento base da base de dados do Sistema:

insert into User values ("admin", "admin", "System Manager");
insert into User values ("user1",  "password", "John Cena");

-- Falta aumentar o número de downloads sempre que alguem faz download
insert into Music (title, artist, year, path, downloads) values ("Fade to black", "Metalica", 1984, "DB/content_server/Metallica_Fade-to-black.mp3", 0);
insert into Music (title, artist, year, path, downloads) values ("Nothing else matters", "Metalica", 1996, "DB/content_server/Metallica_Nothing_Else_Matters.mp3", 0);
insert into Genre (genre) values ("Heavy Metal");
insert into Genre (genre) values ("Trash Metal"); 	-- tirado do wikipedia
insert into Genre (genre) values ("Rock"); 			-- Porque teste
insert into Music_has_Genre values (1,"Heavy Metal");
insert into Music_has_Genre values (1,"Trash Metal");
insert into Music_has_Genre values (2,"Heavy Metal");
insert into Music_has_Genre values (2,"Rock");

select * from Music;
select * from User; -- podiamos adicionar uma opcao "info/account" onde aparecia o nome do user, senao é inutil
select * from Genre;
select * from Music_has_Genre;

-- pesquisa:
drop procedure search;
DELIMITER //
create procedure search(tag varchar(45))
begin
    
	select m.idMusic, m.title, m.artist, m.year, m.downloads from Music m
	join Music_has_Genre h on m.idMusic = h.idMusic
	join Genre g on g.genre = h.genre_name
	where g.genre = tag;

end//
DELIMITER ;

drop procedure genre;
DELIMITER //
create procedure genre(tag int(2))
begin
    
	select g.genre from Genre g
	join Music_has_Genre h on g.genre = h.genre_name
	join Music m on m.idMusic = h.idMusic
	where m.idMusic = tag;

end//
DELIMITER ;

-- update User set name = 'Quim' where username='admin'; 
call genre(1);
CALL search('Heavy Metal');
SELECT DISTINCT g.genre from Music m, Music_has_Genre mg, Genre g WHERE mg.genre_name = g.genre and mg.idMusic = 1;
select g.genre from Genre g, Music_has_Genre h where g.genre = h.genre_name and h.idMusic = 2;