<?php 
// array for json response
$response = array();
$response["nombre"] = array();
$nombre_estado=$_GET['nombre_estado'];
$connection = mysqli_connect("localhost", "root", "","dif");
     
$query = "SELECT * FROM localidades WHERE nombre_estado='$nombre_estado'";
print_r($query);
// Mysql select query
$result = mysqli_query($connection,$query);
print_r ($result);
while($row = mysqli_fetch_array($result)){
    // temporary array to create single category
    $tmp = array();
    $tmp["nombre"] = $row["nombre"];
         
    // push category to final json array
    array_push($response["nombre"], $tmp);
}

print_r($response);
     
// keeping response header to json
header('Content-Type: application/json');
     
// echoing json result
echo json_encode($response);
 
?>