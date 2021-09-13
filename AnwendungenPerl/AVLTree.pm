#!/usr/bin/perl
use lib ".";
use strict;
use warnings;

package AVLTree;

my $nz = "\n";


sub new () {
    my $class_name = shift;
    
    my $blatt;
    my %blatt;
    $blatt{"vorfahre"}=0;
    $blatt{"inks"}=0;
    $blatt{"rechts"}=0;
    $blatt{"balance"} = 0;
    $blatt{"wert-sortieren"} = 2;
    
    $blatt = %blatt;
    
    bless $blatt, $class_name;
    
	print ("AVLTree:new\n");
	

	return $blatt;
	}
;

sub proto (){
    my $self = shift;
    print ("AVLTree:proto\n");
    print ($self."\n");
    }
;

sub blattAnzeigen () {
    print ("AVLTree:blattAnzeigen\n");
    
    my $self = shift;
    my $Blatt= shift;
    
    print "Klasse=".$self.$nz;
	print "Daten=".$nz;
	print ("  Vorfahre   =".%$Blatt{"vorfahre"}.$nz) if defined(%$Blatt{"vorfahre"});
	print ("  Kind Links =".%$Blatt{"links"}.$nz)  if defined(%$Blatt{"links"});
	print ("  Kind rechts=".%$Blatt{"rechts"}.$nz) if defined(%$Blatt{"rechts"});
	print ("  Balance    =".%$Blatt{"balance"}.$nz) if defined(%$Blatt{"balance"});
	print ("    Sortieren=".%$Blatt{"wert-sortieren"}.$nz) if defined(%$Blatt{"wert-sortieren"});
#	print ("    Daten1   =".%$Blatt{"wert-daten1"}.$nz) if defined(%$Blatt{"wert-daten1"});
#	print ("    Daten2   =".%$Blatt{"wert-daten2"}.$nz)if defined(%$Blatt{"wert-daten2"});
}
;


1;

