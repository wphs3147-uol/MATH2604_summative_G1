import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.lang.reflect.*;

/**
 * Non-assessed unit tests. 
 * These tests check whether some of the expected functions have the correct name and signature.
 */
public class DeclarationTest
{
    @Rule
    public Timeout globalTimeout = new Timeout(60000);
    
    private Class<?> doubleArray;
    private Class<?> doubleArray2;

    /**
     * Constructor for this test class.
     */
    public DeclarationTest()
    {
        doubleArray = (new double[0]).getClass();
        doubleArray2 = (new double[0][0]).getClass();
    }
    
    /**
     * helper method to assert that a class is located in the default package
     */
    private void assertDefaultPackage(Class<?> targetClass)
    {
        String msg = targetClass.getName()+" must be in default package";
        if (targetClass.getPackage() != null)
        {
            assertEquals(msg, "", targetClass.getPackage().getName());
        }
    }
    /**
     * helper method to verify the type parameter of a return value
     */
    private void assertReturnTypeParameter(Class<?> targetClass, String methodName, Class typePar, Class... paramType)
    {        
        String fullname = targetClass.getName()+"."+methodName; 
        String tpname = typePar.getName();
        String msg = "return type of "+fullname+" must have type parameter <"+ tpname +">; ";

        try
        {
            Method method = targetClass.getDeclaredMethod(methodName, paramType);
            Type returnType = method.getGenericReturnType();

            if(returnType instanceof ParameterizedType){
                ParameterizedType ptype = (ParameterizedType) returnType;
                Type[] typeArguments = ptype.getActualTypeArguments();
                assertEquals(msg, 1, typeArguments.length);
                assertEquals(msg, typePar, typeArguments[0]);
            }
            else 
            {
                fail(msg);
            }
        }
        catch (NoSuchMethodException e)
        {
            fail("Method "+fullname+" does not exist or has incorrect signature");
        }

    }

    /**
     * helper method for testing whether a (static) function is declared
     */
    private void assertFunctionDeclared(Class<?> targetClass, String methodName, Class returnType, Class... paramType)
    {
        assertDefaultPackage(targetClass);

        String fullname = targetClass.getName()+"."+methodName; 
        try
        {
            Method method = targetClass.getDeclaredMethod(methodName, paramType);

            assertEquals("Return type of "+fullname, returnType, method.getReturnType());

            assertTrue(fullname+" not declared static", Modifier.isStatic(method.getModifiers()) );
            assertFalse(fullname+" must not be declared private", Modifier.isPrivate(method.getModifiers()) );
        }
        catch (NoSuchMethodException e)
        {
            fail("Function "+fullname+" does not exist or has incorrect signature");
        }                

    }

    /**
     * helper method for testing whether a method is declared
     */
    private void assertMethodDeclared(Class<?> targetClass, String methodName, Class returnType, Class... paramType)
    {
        assertDefaultPackage(targetClass);

        String fullname = targetClass.getName()+"."+methodName; 
        try
        {
            Method method = targetClass.getDeclaredMethod(methodName, paramType);

            assertEquals("Return type of "+fullname, returnType, method.getReturnType());

            assertFalse(fullname+" must not be declared static", Modifier.isStatic(method.getModifiers()) );
            assertFalse(fullname+" must not be declared private", Modifier.isPrivate(method.getModifiers()) );
        }
        catch (NoSuchMethodException e)
        {
            fail("Method "+fullname+" does not exist or has incorrect signature");
        }                

    }

    /**
     * helper method for testing whether a constructor is declared
     */
    private void assertConstructorDeclared(Class<?> targetClass, Class... paramType)
    {
        assertDefaultPackage(targetClass);

        String fullname = "Constructor in "+targetClass.getName(); 
        try
        {
            Constructor constructor = targetClass.getConstructor(paramType);

            assertFalse(fullname+" must not be declared private", Modifier.isPrivate(constructor.getModifiers()) );
        }
        catch (NoSuchMethodException e)
        {
            fail(fullname+" does not exist or has incorrect signature");
        }    
    }

    /**
     * helper method for testing whether *no* constructor is declared 
     * (for composite data types)
     */
    private void assertNoConstructorDeclared(Class<?> targetClass)
    {
        assertDefaultPackage(targetClass);

        boolean hasConstructors = targetClass.getConstructors().length > 0;

        String msg = targetClass.getName() + " must not have constructors";

        assertFalse(msg, hasConstructors);
    }

    /**
     * helper method for testing whether a field is declared
     */
    private void assertFieldDeclared(Class<?> targetClass, String fieldName, Class type)
    {
        assertDefaultPackage(targetClass);

        String fullname = targetClass.getName()+"."+fieldName; 
        try
        {
            Field field = targetClass.getDeclaredField(fieldName);

            assertEquals("Type of field "+fullname, type, field.getType());

            assertFalse(fullname+" must not be declared static", Modifier.isStatic(field.getModifiers()) );
            assertFalse(fullname+" must not be declared private", Modifier.isPrivate(field.getModifiers()) );
        }
        catch (NoSuchFieldException e)
        {
            fail("Field "+fullname+" does not exist");
        }      
    }

    /**
     * helper method for testing whether a class is a subclass of another one
     */
    private void assertSubclassOf(Class<?> targetClass, Class<?> superClass)
    {
        assertDefaultPackage(targetClass);

        String msg = "Checking whether "+targetClass.getName()+ " is a subclass of "+superClass.getName();
        assertTrue(msg, superClass.isAssignableFrom(targetClass));
    }


    /**
     * Tests whether the static variable Diagonals.exampleMatrix is declared.
     */
    @Test
    public void diagonalsExampleDeclaredTest()
    {    
        assertFunctionDeclared(Diagonals.class, "exampleMatrix", doubleArray);
        
    }

    /**
     * Tests whether the function Diagonals.sum() is declared.
     */
    @Test
    public void diagonalsSumDeclaredTest()
    {    
        assertFunctionDeclared(Diagonals.class, "sum", doubleArray, doubleArray, doubleArray);
        
    }

    /**
     * Tests whether the function Diagonals.product() is declared.
     */
    @Test
    public void diagonalsProductDeclaredTest()
    {    
        assertFunctionDeclared(Diagonals.class, "product", doubleArray, doubleArray, doubleArray);
        
    }
    
    /**
     * Tests whether the function Diagonals.inverse() is declared.
     */
    @Test
    public void diagonalsInverseDeclaredTest()
    {    
        assertFunctionDeclared(Diagonals.class, "inverse", doubleArray, doubleArray);
        
    }

    /**
     * Tests whether the function Tridiagonals.exampleMatrix() is declared.
     */
    @Test
    public void tridiagonalsExampleDeclaredTest()
    {    
        assertFunctionDeclared(Tridiagonals.class, "exampleMatrix", doubleArray2, int.class);        
    }

    /**
     * Tests whether the function Tridiagonals.isValidTridiagonal() is declared.
     */
    @Test
    public void tridiagonalsIsValidTridiagonalDeclaredTest()
    {    
        assertFunctionDeclared(Tridiagonals.class, "isValidTridiagonal", boolean.class, doubleArray2);
        
    }

    /**
     * Tests whether the function Tridiagonals.sum() is declared.
     */
    @Test
    public void tridiagonalsSumDeclaredTest()
    {    
        assertFunctionDeclared(Tridiagonals.class, "sum", doubleArray2, doubleArray2, doubleArray2);
        
    }

    /**
     * Tests whether the function Tridiagonals.productWithDiagonal() is declared.
     */
    @Test
    public void tridiagonalsProductWithDiagonalDeclaredTest()
    {    
        assertFunctionDeclared(Tridiagonals.class, "productWithDiagonal", doubleArray2, doubleArray, doubleArray2);
        
    }
    
    /**
     * Tests whether the function Tridiagonals.linearSolve() is declared.
     */
    @Test
    public void tridiagonalsLinearSolveDeclaredTest()
    {    
        assertFunctionDeclared(Tridiagonals.class, "linearSolve", doubleArray, doubleArray2, doubleArray);
        
    }

    /**
     * Tests whether the function ODE.solve() is declared.
     */
    @Test
    public void odeSolveDeclaredTest()
    {    
        assertFunctionDeclared(ODE.class, "solve", double.class, double.class, int.class);
        
    }
        

}
