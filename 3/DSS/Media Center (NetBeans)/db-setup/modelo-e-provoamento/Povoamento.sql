
use MediaCenterDB;

#drop schema MediaCenterDB;

#Para remover todo o conteúdo das tabelas.

delete from AlbunsDoUtilizador;
delete from ConteudoDoAlbum;
delete from Conteudo;
delete from AmigosDoUtilizador;
delete from Amigo;
delete from Utilizador;
delete from Album;
delete from Categoria;

#Povoamento base da base de dados do Sistema:

insert into Utilizador values ("user1", "Utilizador1", "", 0, 0);
insert into Utilizador values ("user2", "Utilizador2", "", 0, 0);
insert into Utilizador values ("user3", "Utilizador3", "password", 0, 0);

insert into Categoria values ("Rock");
insert into Categoria values ("Metal");
insert into Categoria values ("Pop");
insert into Categoria values ("Blues");
insert into Categoria values ("Jazz");
insert into Categoria values ("CategoriaTeste");
insert into Categoria values ("Nenhuma");
insert into Categoria values ("Video");

insert into Album values ("AlbumUser1", "Rock");
insert into Album values ("AlbumUser2", "Metal");

insert into AlbunsDoUtilizador (idUserADU, idAlbumADU) values ("user1", "AlbumUser1");
insert into AlbunsDoUtilizador (idUserADU, idAlbumADU) values ("user2", "AlbumUser2");
            
insert into Conteudo values ("Metallica_Fade-to-black.mp3", "Metallica", 1, 0, "DB/Content/Metallica_Fade-to-black.mp3", "Metal");
insert into Conteudo values ("SampleVideo.mp4", "SampleVideo", 0, 1, "DB/Content/SampleVideo.mp4", "Video");

insert into Conteudo values ("Odyssey.mp3", "Odyssey", 1, 0, "DB/Content/Odyssey.mp3", "Pop");
insert into Conteudo values ("RolandCLine01.mp3", "Roland", 1, 0, "DB/Content/RolandCLine01.mp3", "Pop");
insert into Conteudo values ("Solina.mp3", "Solina", 1, 0, "DB/Content/Solina.mp3", "Pop");

insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) values ("AlbumUser1", "Metallica_Fade-to-black.mp3", "Metal");
insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) values ("AlbumUser1", "SampleVideo.mp4", "Video");

insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) values ("AlbumUser2", "Odyssey.mp3", "Pop");
insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) values ("AlbumUser2", "RolandCLine01.mp3", "Pop");
insert into ConteudoDoAlbum (idAlbumCDA, idConteudoCDA, idCategoriaCDA) values ("AlbumUser2", "Solina.mp3", "Pop");
