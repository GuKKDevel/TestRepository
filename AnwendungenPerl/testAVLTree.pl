#!/usr/bin/perl
use lib ".";
use strict;
use warnings;
use AVLTree;

print "hi NAME\n";
my $baum = AVLTree->new();
#my $x=AVLTree->proto();
AVLTree->blattAnzeigen($baum);

1;

