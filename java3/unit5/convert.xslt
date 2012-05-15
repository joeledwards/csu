<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output encoding="UTF-8" method="xml" omit-xml-declaration="yes"/>
    <xsl:template match="/">
        <html>
            <body>
                <table>
                <tr>
                    <th>Year</th>
                    <th>Author</th>
                    <th>Title</th>
                    <th>Publisher</th>
                    <th>ISBN</th>
                    <th>Price</th>
                </tr>
                <xsl:for-each select="inventory/book">
                    <tr>
                        <td><xsl:value-of select="@year"/></td>
                        <td><xsl:value-of select="@author"/></td>
                        <td><xsl:value-of select="title"/></td>
                        <td><xsl:value-of select="publisher"/></td>
                        <td><xsl:value-of select="isbn"/></td>
                        <xsl:param name="title" select="price"/>
                        <td>
                            <xsl:value-of select="price"/>
                            <xsl:if test="$title &lt;= '6.0'">!!!</xsl:if>
                        </td>
                    </tr>
                </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>

