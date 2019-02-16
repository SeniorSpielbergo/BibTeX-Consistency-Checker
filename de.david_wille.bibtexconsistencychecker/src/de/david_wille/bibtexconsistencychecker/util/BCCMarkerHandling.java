package de.david_wille.bibtexconsistencychecker.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.validation.DiagnosticConverterImpl;

public class BCCMarkerHandling extends DiagnosticConverterImpl {

	public void createErrorMarker(IResource resource, String message, EObject eObject, EStructuralFeature eStructuralFeature) {
		try {
			IssueLocation location = getLocationData(eObject, eStructuralFeature);
			
			IMarker marker = resource.createMarker(IMarker.PROBLEM);
			
			Map<String, ? super Object> map = createMarkerAttributeMap(message, marker, location);
			marker.setAttributes(map);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void createErrorMarker(IResource resource, String message, EObject eObject, EStructuralFeature eStructuralFeature, int index) {
		try {
			IssueLocation location = getLocationData(eObject, eStructuralFeature, index);
			
			IMarker marker = resource.createMarker(IMarker.PROBLEM);
			
			Map<String, ? super Object> map = createMarkerAttributeMap(message, marker, location);
			marker.setAttributes(map);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private Map<String, ? super Object> createMarkerAttributeMap(String message, IMarker marker, IssueLocation location) throws CoreException {
		Map<String, ? super Object> map = new HashMap<>();
		
		map.put(IMarker.LINE_NUMBER, location.lineNumber);
		map.put(IMarker.CHAR_START, location.offset);
		map.put(IMarker.CHAR_END, location.offset + location.length);
		map.put(IMarker.MESSAGE, message);
		map.put(IMarker.SEVERITY, IMarker.PRIORITY_HIGH);
		
		return map;
	}

	public static void clearAllExistingErrorMarkers(List<IFile> files) {
		for (IFile file : files) {
			clearAllExistingErrorMarkers(file);
		}
	}

	public static void clearAllExistingErrorMarkers(IFile file) {
		try {
			file.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

}
