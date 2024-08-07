<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tipo de Contrataciones</title>
</head>
<body>
<h2>Lista de Tipos de Contratación</h2>
<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Tipo de Contratación</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="tipoContratacion" items="${tipoContrataciones}">
        <tr>
            <td>${tipoContratacion.idTipoContratacion}</td>
            <td>${tipoContratacion.tipoContratacion}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
