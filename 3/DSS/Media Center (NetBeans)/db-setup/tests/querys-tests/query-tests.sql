
#Selecionar o conteúdo de um utilizador

select distinct cda.*, c.Categoria_idNomeCategoria from Utilizador u, AlbunsDoUtilizador adu, Album a, ConteudoDoAlbum cda, Conteudo c
where u.email = adu.idUserADU and adu.idAlbumADU = a.idAlbum and cda.idAlbumCDA = a.idAlbum and cda.idConteudoCDA = c.idConteudo and c.Categoria_idNomeCategoria = cda.idCategoriaCDA
and u.email = "user2";

select * from Conteudo;

select a.emailAmigo from Utilizador u, Amigo a, AmigosDoUtilizador adu
where u.email = adu.Utilizador_email and adu.Amigo_emailAmigo = a.emailAmigo and u.email = "user2";

delete from ConteudoDoAlbum where idAlbumCDA = "album1" and idConteudoCDA = "music_upload_1.mp3" and idCategoriaCDA = "Nenhuma";
insert into Conteudo values ("music_upload_1.mp3", "music_upload_1.mp3", 1, 0, "path", "Metal");
insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) values ("album1", "music_upload_1.mp3", "Metal");

delete from Album where idAlbum = "Músicas de Rock";
delete from AlbunsDoUtilizador where idAlbumADU = "Músicas de Rock";

#group by c.Categoria_idNomeCategoria
#order by count(c.Categoria_idNomeCategoria) desc;
    
select * from Conteudo c;

select a.idAlbum from Utilizador u, AlbunsDoUtilizador adu, Album a
where u.email = adu.idUserADU and adu.idAlbumADU = a.idAlbum and u.email = "test";

#ROLLBACK APÓS UPLOAD /DB-Setup/uploadtest

delete from ConteudoDoAlbum where idConteudoCDA = "music_upload_1.mp3";
delete from ConteudoDoAlbum where idConteudoCDA = "music_upload_2.mp3";
delete from Conteudo where idConteudo = "music_upload_1.mp3";
delete from Conteudo where idConteudo = "music_upload_2.mp3";
delete from AlbunsDoUtilizador where idAlbumADU = "albumnovo";
delete from Album where idAlbum = "albumnovo";

#OUTROS

select count(*) from Conteudo c where c.idConteudo = "Odyssey.mp3" and c.Categoria_idNomeCategoria = "asd";
select adu.Utilizador_email as User, adu.Amigo_emailAmigo as Amigo from Amigo a, AmigosDoUtilizador adu where a.emailAmigo = adu.Amigo_emailAmigo;
select * from Utilizador;
select * from Conteudo where idConteudo = "Odyssey.mp3";
select * from AlbunsDoUtilizador;
select * from ConteudoDoAlbum;




/*
delete from ConteudoDoAlbum where idAlbumCDA = "AlbumUser2" and idConteudoCDA = "Solina.mp3" and idCategoriaCDA = "Pop";
update Conteudo set Categoria_idNomeCategoria = "Rock" where idConteudo = "Solina.mp3" and Categoria_idNomeCategoria = "Pop";
insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) values ("AlbumUser2", "Solina.mp3", "Rock");
*/


