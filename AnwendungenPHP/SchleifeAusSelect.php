$query ="SELECT * FROM EintragLand";
$sql = mysql_query($query) or 
	die ('Fehler beim Zugriff');

while ($dbs = mysql_fetch_object($sql)) {
echo "hallo<br>";
	$ELK = $dbs -> EL_Kuerzel;
	echo $ELK."<br>";

}
echo "hallo2";
mysql_close($dbz);