Client-Server Application
==============

Potrebno je u Javi napraviti klijentsku I serversku aplikaciju za obradu reci prema sledecim parametrima.
 
Klijent:
--------------
-          Graficki interfejs. Prozor sa jednim dugmetom, progress bar-om i text area-om.
-          Na klik salje serveru 200 random generisanih reci (recimo do 20 karaktera duzine)
-          Progress bar se puni
-          Po zavrsenoj obradi reci, klijent izracunava I u text area ispisuje statistiku: ukupna duzina obrade, prosecna duzina obrade reci, najveca duzina obrade I koja rec je bila u pitanju, najmanja duzina obrade I koja rec je bila u pitanju.
 
 
Server:
--------------
-          Obradjuje reci koje prima od klijenta. Svaka rec se obradjuje 0.1 sek na serveru (rec od 4 slova = 400ms, 6 slova 600ms).
-          Obrada reci se radi u 2 thread pool-a, jedan za reci sa parnim brojem slova, drugi za reci sa neparnim brojem slova
-          Kada se zavrsi obrada reci, rec se salje nazad klijentu
-          Redosled slanja rezultata klijentu mora da bude saglasan redosledu zahteva od klijenta (ne smem klijentu da vratim drugu rec ako prvu nisam obradio)
-          Kada se zavrsi obrada, server perzistira u bazu informacije o raznim statistikama (najduza obrada, najkraca obrada, prosecna obrada, ukupno trajanje)
 
Database:
--------------
-          Samo server komunicira sa bazom.
-          Baza sadrzi dve tabele: BATCH i WORD.
-          Batch je skup svih reci koje klijent posalje serveru. U toj tabeli treba uskladistiti statisticke podatke koje server salje klijentu (ukupan broj reci, prosecna duzina, itd.)
-          WORD tabela pamti pojedinacne reci. Sadrzi kolone za same reci, duzinu obrade te reci i oznaku da li pripada parnim ili neparnim recima. Takodje je svaka rec jasno definisana da pripada nekom Batch-u.
 
U redu je pretpostaviti da ce server I klijent biti pokrenuti na istoj masini. Takodje je u redu pretpostaviti da ce samo jedan klijent raditi u jednom trenutku. 