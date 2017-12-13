package org.magapov.equinox.surricatalog;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator{
	private FileWorker fw; 
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Bundle started");
		fw = new FileWorker();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		fw.interrupt();
		System.out.println("Bundle stopped");
	}
	
}
