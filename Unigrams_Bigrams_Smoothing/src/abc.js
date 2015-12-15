oracledb.getConnection({
	// The connection details to my database
	user : "nodejs",
	password : "access",
	connectString : "localhost:1521/TEST"
}, function(err, connection) {
	// If an error happens during establishing the connection, print the error
	// message and return (exit the program)
	if (err) {
		console.error(err.message);
		return;
	}
	// Executing my SQL SELECT statement against the departments table
	connection.execute("SELECT department_id, department_name, manager_id "
			+ "FROM departments " + "WHERE location_id = :lid",
	// This is the parameter for the bind variable ":lid" that I have used in my
	// WHERE clause.
	[ 1700 ], function(err, result) {
		// If an error happens during the SQL execution, print the error message
		// and return (exit the program)
		if (err) {
			console.error(err.message);
			return;
		}
		// Print the results into the console
		console.log(result.rows);
	});
});