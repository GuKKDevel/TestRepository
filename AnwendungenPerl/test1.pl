#!/usr/bin/perl -w
#
use strict;
use warnings;

my $verzeichnis = "Wachst:uchschmel?zen.odt";
$verzeichnis =~ s/:/xDpX/g;
print "$verzeichnis\n";

$verzeichnis =~ s/\?/xFzX/g;
print "$verzeichnis\n";

$verzeichnis =~ s/xDpX/:/g;
print "$verzeichnis\n";

$verzeichnis =~ s/xFzX/?/g;
print "$verzeichnis\n";

