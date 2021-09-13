#!/usr/bin/perl -w
#
#
use strict;
use warnings;
use Env qw(USER);
use autodie;
# Parameter:
#	
#	Art des Verarbeitung:
#		"sichern" wenn gesichert werden soll
#		"vergleichen" wenn die Verzeichnisse verglichen
#		"zurück" wenn die Daten zurückgeschrieben werden sollen
#
#--------------------------------------------------------------------------------------------
#	
# Programmverwendung:
#
#	Das Programm dient dem einfachen Kopieren von bestimmten Dateien auf einen transportablen
#	Datenspeicher. 
#	
#	Dabei werden bei Verarbeitung "sichern"" die Daten vom OriginalVerzeichnis in ein gleich-
#	namiges Verzeichnis auf dem Transportmedium geschrieben. Sollte dieses Verzeichnis bereits 
#	existieren, wird das Programm abgebrochen. und das Verzeichnis muss vorher manuell gelöscht 
#	werden.
#		Momentan werden folgende Verzeichnisse berücksichtigt:
#			GEMEINSAM
#			programmieren
#			gukkdevel
#			khg
#			uk
#
#
#	Das Originalverzeichnis wird mit "/home/" zwingend vorgeschrieben.
#	
#	Das Zielverzeichnis wird zwingend mit "/media/khg/KHG1TB/" vorgeschrieben
#
#	Neben den kopierten Verzeichnissen/Dateien wird im Zielverzeichnis beim 
#		Sichern ein Ordner meinkopieren-log eingerichtet,
#		Dieser enthält je Zielverzeichnis eine Datei mit den zugehörigen metadaten für die einzelnen Dateien
#				String: Dateiart D/V
#				String: Leerzeichen
#				String: Dateiname
#				String: Leerzeichen
#				Datum:  Datum der letzten Änderung (aus den Attributen)
#				String: Leerzeichen
#				Char:   Kennzeichen ob beim Sichern der Dateiname geändert wurde
#				String: Leerzeichen
#				String: geänderter Dateiname
#	
#
#--------------------------------------------------------------------------------------------
# Argumente:	
#my @zuSichern = ("GEMEINSAM", "programmieren", "gukkdevel","khg", "uk");
# T E S T

my @zuSichern = ("test"); #, "test1", "test2");
foreach my $xverzeichnis (@zuSichern)  {
	if (-e "/media/KHG1TB/".$xverzeichnis) {
		system ("rm -R \/media/KHG1TB/$xverzeichnis");
		print "/media/KHG1TB/".$xverzeichnis."\n";
		print "Verzeichnis gelöscht\n";
	}
}
print "Ende der Test Sonderbefehle\n\n";
# T E S T   Ende
#
# Standardvariablen 
#
my $nz = "\n";
my $logzeile = "";
#
# Globale Variablen
#
our $endeKennzeichen = 0;
our $Quelle = "/home/";
our $Ziel = "/media/KHG1TB/";
#
# Lokale Variablen
#
#my $quellVerzeichnis = "";
my $zielVerzeichnis= "";
my $metaVerzeichnis = "";


&pruefenParameter;

if ($endeKennzeichen){
	print "Programm wird nach fehlerhafter Vorprüfungen beendet";
	exit;
}
else {
	print "Verarbeitung wird begonnen$nz$nz";
}

if ($ARGV[0] eq "sichern") {
	foreach my $verzeichnis (@zuSichern){
		&verzeichnisSichern ($verzeichnis);
	}
}
else {
	foreach my $verzeichnis (@zuSichern) {
#		&verzeichnisZurueck ($verzeichnis);
	}
}


print $nz.$endeKennzeichen;
print " Programmende".$nz.$nz;

#
#
#	Subroutine zur überprüfung der notwendigen Verzeichnisse bei
#	der Sicherung der Daten
#
#
sub verzeichnisSichern {
#
# 	Globale Variablen
#
	our $Quelle;
	our $Ziel;
#
# 	Hilfsvariablen
#

	my $verzeichnis = shift;
	my $quellVerzeichnis = $Quelle.$verzeichnis;
	my $zielVerzeichnis = $Ziel.$verzeichnis;
	my $metaVerzeichnis = $Ziel.$verzeichnis."/meinkopieren-log";
	

#
# Start Subroutine
#
	print "Verarbeitet wird das Verzeichnis $quellVerzeichnis$nz";

#
# Überprüfen der Ziel-Verzeichnisse
#
	if (! -e $zielVerzeichnis ) {
		print "$zielVerzeichnis existiert nicht, wird angelegt ".$nz;
		mkdir $zielVerzeichnis or die "$zielVerzeichnis kann nicht angelegt werden: $! $nz";
		print "$metaVerzeichnis existiert nicht, wird angelegt ".$nz;
		mkdir $metaVerzeichnis or die "$metaVerzeichnis kann nicht angelegt werden: $! $nz";
	}
	else{
		if (! -d $zielVerzeichnis) {
			die "$zielVerzeichnis ist kein Verzeichnis$nz"
		}
		else {
			if (! -e $metaVerzeichnis) {
				print "$metaVerzeichnis existiert nicht, wird angelegt ".$nz;
				mkdir $metaVerzeichnis or die "$metaVerzeichnis kann nicht angelegt werden: $! $nz";
			}
			else {
				if (! -d $metaVerzeichnis){
					die "$metaVerzeichnis ist kein Verzeichnis$nz";
				}
			}
		}
	}

#
# Anlegen der Meta-Logdatei
#
	our $metaLog;
	open ($metaLog, ">", "$metaVerzeichnis/logfile") or die "Kann $metaVerzeichnis/logfile nicht öffnen $!";
	
	print "Logdatei $metaVerzeichnis/logfile wurde erstellt$nz";
#
# Lesen des Quellverzeichnis
#
	&sichernUnterverzeichnis ($verzeichnis);

	close $metaLog;
	print "Logdatei $metaVerzeichnis/logfile wurde geschlossen$nz";
	print "Verarbeitung für Verzeichnis $quellVerzeichnis wurde beendet".$nz.$nz;
} # sub verzeichnisseSichern

#
#
#	Subroutine zum bearbeiten der Unterverzeichnisse beim sichern
#
#
sub sichernUnterverzeichnis {
#
#	Globale Variablen
#
	our $Quelle;
	our $Ziel;
#
#	Lokale Variablen
#
	my $dateiname = "";
#
#	Beginn der Verarbeitung
#
	my $verzeichnis = shift;
	my $verzeichnismod = $verzeichnis;
	$verzeichnismod =~ s/:/xDpX/g; # Ersetzen Doppelpunkt : durch xDpX
	$verzeichnismod =~ s/\?/xFzX/g; # Ersetzen Fragezeichen ? durch xFzX
	
	if (! -e "$Ziel$verzeichnismod" ||  -d "$Ziel.$verzeichnismod") {
		mkdir "$Ziel$verzeichnismod" or die "Fehler beim Anlegen von $Ziel$verzeichnismod $!";
		
	}
	opendir (my $vv, "$Quelle$verzeichnis") or die "Fehler beim öffnen von $Quelle$verzeichnis $! ";
	while (readdir $vv) {
		$dateiname = $_;
		if ($dateiname eq "\." || $dateiname eq "\.\.") {
			next;
		} 
		my $dateinamemod = $dateiname;
		$dateinamemod =~ s/:/xDpX/g; # Ersetzen Doppelpunkt : durch xDpX
		$dateinamemod =~ s/\?/xFzX/g; # Ersetzen Fragezeichen ? durch xFzX
		if ( -d "$Quelle$verzeichnis/$dateiname") {
			&protokollMetalog ("V", "$Quelle$verzeichnis/$dateiname", "$Ziel$verzeichnismod/$dateinamemod");
			&sichernUnterverzeichnis ("$verzeichnis/$dateiname");
		}
		elsif ( -f "$Quelle$verzeichnis/$dateiname") {
			system qq{cp --preserve '$Quelle$verzeichnis/$dateiname' '$Ziel$verzeichnismod/$dateinamemod'}; # Kopieren der Datei 
			if ($?) {
				print "Fehler beim kopieren: $?";
			}
			&protokollMetalog ("D", "$Quelle$verzeichnis/$dateiname", "$Ziel$verzeichnismod/$dateinamemod");
		}
	}
	closedir $vv;




} #sub sichernUnterverzeichnis;

#
#
#	Subroutine zum protokollieren der Vorgänge (MetaLog)
#
#
sub protokollMetalog {
#
#	Globale Variablen
#
	our $metaLog;
#-------------------------------------------------------------------------------------
#
#	Datensatzstruktur für Logdatei
#
#				String: Dateiart D/V
#				String: Leerzeichen
#				String: Dateiname
#				String: Leerzeichen
#				Datum:  Datum der letzten Änderung (aus den Attributen)
#				String: Leerzeichen
#				Char:   Kennzeichen ob beim Sichern der Dateiname geändert wurde
#				String: Leerzeichen
#				String: geänderter Dateiname
	
	my $dateiart = shift;
	my $dateinameQuelle = shift;
	my $dateinameZiel = shift;
	
	print $metaLog $dateiart." ";
	print $metaLog $dateinameQuelle." ";
#	print $metaLog $datum." ";
	if (substr('$dateinameQuelle', length($Quelle)) ne substr('$dateinameZiel', length($Ziel))) {	
		print $metaLog "V ";
		print $metaLog $dateinameZiel." ";
	}
	print $metaLog $nz;
	
} #sub protokollMetalog

#
#
#	Subroutine zur überprüfung der notwendigen Verzeichnisse bei
#	# TODO verzeichnisZurueck Programmieren
#
#
sub verzeichnisZurueck{
	# TODO verzeichnisZurueck Programmieren
	
#	$verzeichnismod =~ s/xDpX/:/g; # Ersetzen  xDpX durch Doppelpunkt : 
#	$verzeichnismod =~ s/xFzX/\?/g; # Ersetzen  xFzX durch Fragezeichen ? 
} # sub verzeichnisZurueck
#
#
#	Subroutine zur überprüfung der notwendigen Verzeichnisse bei
#	der Rückübertragung der Daten
#	# TODO pruefenZurueck Programmieren
#
sub pruefenZurueck {  

# TODO pruefenZurueck Programmieren
	
} #sub pruefenZurueck
#
#
# Prüfen der übergebenen Parameter
#
#
sub pruefenParameter {
# Globale Variable
	our $Quelle;
	our $Ziel;
	our $endeKennzeichen;
# Lokale Variable
	
	if (@ARGV < 1) {
		print "Parameter fehlt; mindestens ein Parameter mit dem Inhalt \"sichern\", \"vergleichen\" oder \"zurück\" ist notwendig$nz";
		$endeKennzeichen = 1;
	}
	elsif (@ARGV == 1) {
		if ($ARGV[0] eq "sichern") {
				foreach my $verzeichnis (@zuSichern) {
					if (-e "$Quelle$verzeichnis"){
						if (! -d "$Quelle$verzeichnis") {
							print " ist aber kein Verzeichnis --> Abbruch$nz";
							$endeKennzeichen = 1;
						}
						if (! -r "$Quelle$verzeichnis") {
							print " kann aber nicht gelesen werden --> Abbruch$nz";
							$endeKennzeichen = 1;
						}
						print $nz;
					}
					else{
						print "existiert nicht --> Abbruch$nz";
						$endeKennzeichen = 1;
					}
				}
				if ($endeKennzeichen){
					print "Es traten Fehler auf$nz$nz";
				}
				else {
					print "Alle zu sichernden Verzeichnisse sind vorhanden$nz$nz" 
				}
		}
		elsif ($ARGV[0] eq "vergleichen"){
			
		}
		elsif ($ARGV[0] eq "zurück") {
			
		}
		else {
			print "Parameter ungültig; erlaubt sind \"sichern\", \"vergleichen\" und \"zurück\".$nz";
			$endeKennzeichen = 1;
		}
	}
	else {
		print "zu viele Parameter";
		$endeKennzeichen = 1;
	}
} # sub pruefenParameter
#
# Ende des Programms
#