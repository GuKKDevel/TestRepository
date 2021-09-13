#!/usr/bin/perl -w
#
use strict;
use warnings;

my %BenannteListe ;

$BenannteListe{"vorfahre"} = "Vf";
$BenannteListe{"links"} = "Kl";
$BenannteListe{"rechts"} = "Kr";

foreach my $wert (keys %BenannteListe) {
    print $wert." = ".$BenannteListe{$wert}."\n";
    }

print %BenannteListe."\n";

foreach (@INC)  {print "$_ \n"};