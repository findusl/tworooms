<?php 
$db = new mysqli('rdbms.strato.de', 'U2285767', '', 'DB2285767');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}

$since = $_GET['since'];
$version = $_GET['v'];
//TODO: check version

function echoTable($db, $since, $table) {
	$result = mysqli_query($db, 'SELECT * FROM '$table' WHERE last_modified > '$since);
	//http://codular.com/php-mysqli
	
	$jsonObject['table'] = $table;
	
	$result->free();
}

echoTable($db, $since, 'categories');
echoTable($db, $since, 'roles');
echoTable($db, $since, 'sets');
echoTable($db, $since, 'set_roles');
echoTable($db, $since, 'teams');

mysqli_close($con);
?>