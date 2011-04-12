package vjo.java.lang.reflect;

/*
 * @(#)src/classes/sov/java/lang/reflect/AnnotatedElement.java, reflect, asdev, 20070119 1.3
 * ===========================================================================
 * Licensed Materials - Property of IBM
 * "Restricted Materials of IBM"
 *
 * IBM SDK, Java(tm) 2 Technology Edition, v5.0
 * (C) Copyright IBM Corp. 1998, 2005. All Rights Reserved
 * ===========================================================================
 */

/*
 * ===========================================================================
 (C) Copyright Sun Microsystems Inc, 1992, 2004. All rights reserved.
 * ===========================================================================
 */

/*
 * @(#)AnnotatedElement.java	1.3 04/02/03
 *
 */

import vjo.java.lang.* ;

import java.lang.annotation.Annotation;

/**
 * Represents an annotated element of the program currently running in this
 * VM.  This interface allows annotations to be read reflectively.  All
 * annotations returned by methods in this interface are immutable and
 * serializable.  It is permissible for the caller to modify the
 * arrays returned by accessors for array-valued enum members; it will
 * have no affect on the arrays returned to other callers.
 *
 * <p>If an annotation returned by a method in this interface contains
 * (directly or indirectly) a {@link Class}-valued member referring to
 * a class that is not accessible in this VM, attempting to read the class
 * by calling the relevant Class-returning method on the returned annotation
 * will result in a {@link TypeNotPresentException}.
 *
 * <p>Similarly, attempting to read an enum-valued member will result in
 * a {@link EnumConstantNotPresentException} if the enum constant in the
 * annotation is no longer present in the enum type.
 * 
 * <p>Finally, Attempting to read a member whose definition has evolved
 * incompatibly will result in a {@link
 * java.lang.annotation.AnnotationTypeMismatchException} or an
 * {@link java.lang.annotation.IncompleteAnnotationException}.
 *
 * @since 1.5
 * @author Josh Bloch
 */
public interface AnnotatedElement {
    /**
     * Returns true if an annotation for the specified type
     * is present on this element, else false.  This method
     * is designed primarily for convenient access to marker annotations.
     *
     * @param annotationType the Class object corresponding to the
     *        annotation type
     * @return true if an annotation for the specified annotation
     *     type is present on this element, else false
     * @throws NullPointerException if annotationType is null
     * @since 1.5
     */
     boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

   /**
     * Returns this element's annotation for the specified type if
     * such an annotation is present, else null.
     *
     * @param annotationType the Class object corresponding to the 
     *        annotation type
     * @return this element's annotation for the specified annotation type if
     *     present on this element, else null
     * @throws NullPointerException if annotationType is null
     * @since 1.5
     */
    <T extends Annotation> T getAnnotation(Class<T> annotationType);

    /**
     * Returns all annotations present on this element.  (Returns an array
     * of length zero if this element has no annotations.)  The caller of
     * this method is free to modify the returned array; it will have no
     * effect on the arrays returned to other callers.
     *
     * @return all annotations present on this element
     * @since 1.5
     */
    Annotation[] getAnnotations();

    /**
     * Returns all annotations that are directly present on this
     * element.  Unlike the other methods in this interface, this method
     * ignores inherited annotations.  (Returns an array of length zero if
     * no annotations are directly present on this element.)  The caller of
     * this method is free to modify the returned array; it will have no
     * effect on the arrays returned to other callers.
     *
     * @return All annotations directly present on this element
     * @since 1.5
     */
    Annotation[] getDeclaredAnnotations();
}
