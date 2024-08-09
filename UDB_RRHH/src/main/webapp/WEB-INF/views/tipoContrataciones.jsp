<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Tipos de Contratación</title>
</head>
<body>
<h2>Gestión de Tipos de Contratación</h2>

<h3>Agregar Tipo de Contratación</h3>
<form action="TipoContratacionController" method="post">
    <input type="hidden" name="action" value="crear">
    <label for="tipoContratacion">Tipo de Contratación:</label>
    <input type="text" name="tipoContratacion" required>
    <input type="submit" value="Agregar">
</form>

<h3>Lista de Tipos de Contratación</h3>
<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Tipo de Contratación</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="tipoContratacion" items="${tipoContrataciones}">
        <tr>
            <td>${tipoContratacion.idTipoContratacion}</td>
            <td>${tipoContratacion.tipoContratacion}</td>
            <td>
                <a href="TipoContratacionController?action=editar&id=${tipoContratacion.idTipoContratacion}">Editar</a> |
                <a href="TipoContratacionController?action=eliminar&id=${tipoContratacion.idTipoContratacion}" onclick="return confirm('¿Estás seguro de que deseas eliminar este registro?');">Eliminar</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<c:if test="${not empty tipoContratacionEdicion}">
    <h3>Editar Tipo de Contratación</h3>
    <form action="TipoContratacionController" method="post">
        <input type="hidden" name="action" value="actualizar">
        <input type="hidden" name="idTipoContratacion" value="${tipoContratacionEdicion.idTipoContratacion}">
        <label for="tipoContratacion">Tipo de Contratación:</label>
        <input type="text" name="tipoContratacion" value="${tipoContratacionEdicion.tipoContratacion}" required>
        <input type="submit" value="Actualizar">
    </form>
</c:if>

</body>
</html>
