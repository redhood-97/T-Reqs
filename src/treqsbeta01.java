/* 
Anyone can feel free to clone the repository and contribute to improve the project 
overall. Right now, it works for a very standard way of generating dockerfiles of
minimal size for Python and Go projects. However, the code is still subject to test and
further development into a usable JAR file. Any suggestions for generalizing it 
further are more than welcome.

JUST DON'T WRECK ME FOR NOT WRITING BEAUTIFUL CODE :-P

IT MAY GET A LITTLE BIT SLOPPY !!!!  CAUTION !!!!
*/

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class treqsbeta01
{
	public static void main(String[] args) throws Exception
	{
		Scanner scan = new Scanner(System.in);

		System.out.println();

		treqs_banner();

		System.out.println();

		System.out.println("Enter the name(path) of the file:");

		String file_name = scan.nextLine();

		String the_file_extension = new String();

		the_file_extension = get_extension(file_name);
		// Retrieved the extension



		String test_dependencies = new String();

		//    The conversion of file to string begins //

		FileReader f_reader = new FileReader(file_name);

		BufferedReader b_reader = new BufferedReader(f_reader);

		String converted_string = "";

		String line = b_reader.readLine();

		while(line != null)
		{
			converted_string+= line;

			converted_string = converted_string +"\n";  // This line was added to resolve a bug

		//	converted_string+=System.getProperty("line.terminator");

			line = b_reader.readLine();
		}

		//////////////////////////////////////////////////////////////////////////////
		///////////   Getting the information about package installers   ////////////
		////////////////////////////////////////////////////////////////////////////

		String[] package_installer_info = package_recognizer(the_file_extension);

		/////////////////////////////////////////////////////////////////////////////
		///////////     Getting the prepocessing keyword     ///////////////////////
		///////////////////////////////////////////////////////////////////////////

		String preprocess_keyword = new String();

		preprocess_keyword = getting_preprocess(the_file_extension);

		/////////////////////////////////////////////////////////////////////////////
		////////////         TO count the no. of dependencies   ////////////////////
		///////////////////////////////////////////////////////////////////////////

		//String delimiter_value ="\\r?\\n";

		String[] broken_string = converted_string.trim().split("\\r?\\n|\\s+|\\t", -1);

		/*System.out.println("The broken strings are :" + System.lineSeparator());

		for(int i = 0; i < broken_string.length; i++)
		{
			System.out.println(broken_string[i]);
		} */

		String import_string_val = preprocess_keyword;   // We can use it from here.


        ///////////////////////////////////////////////////////////////////////////////
        // Storing the depenedencies separately and counting the no fo dependencies // 
        /////////////////////////////////////////////////////////////////////////////

		String[] dependencies_list_final = dependencies_acquire(broken_string,the_file_extension);

		int no_of_dependencies = dependencies_list_final.length;
		
		/*
		System.out.println("The no of dependencies are : "+ no_of_dependencies);

		System.out.println("The dependencies required in this file are:");

		for(int i = 0; i < no_of_dependencies; i++)
		{
			System.out.println(dependencies_list_final[i]);
		}
		*/

		b_reader.close();

		/////////////////////////////////////////////////////////////////////////////
		////////////// Creating a master app directory /////////////////////////////
		///////////////////////////////////////////////////////////////////////////

		String master_folder_name = new String();

		System.out.println("Enter a name for the new project app directory :");

		master_folder_name = scan.nextLine();

		File master_app_dir = new File(master_folder_name);

		if(master_app_dir.exists())
		{
			master_app_dir.delete();
		}

		try
		{
			master_app_dir.mkdir();

			System.out.println("Master app directory has been created !!!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		/////////////////////////////////////////////////////////////////////////////
		////////         First we need to create an SRC directory          /////////
		////////////////////////////////////////////////////////////////////////////

		File src_dir = new File(master_folder_name + "/src");

		if(src_dir.exists())
		{
			src_dir.delete();
		}

		try
		{
			src_dir.mkdir();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		/////////////////////////////////////////////////////////////////////////////
		////////////   Copying the code file into the src directory    /////////////
		///////////////////////////////////////////////////////////////////////////

		File code_file = new File(file_name);

		File destination_code_file = new File(master_folder_name + "/src/" + file_name);

		try
		{	
			if(!destination_code_file.exists())
			{
				destination_code_file.createNewFile();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			Files.copy(code_file.toPath(), destination_code_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	    //////////////////////////////////////////////////////////////////////////////
	    //////      Creating the requirements folder and writing into it      ///////
	    ////////////////////////////////////////////////////////////////////////////


		File new_req_file = new File(master_folder_name + "/src/requirements.txt");

		if(new_req_file.exists())
		{
			new_req_file.delete();
		}
			
		try
		{
			new_req_file.createNewFile();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			FileWriter write_file = new FileWriter(new_req_file);

			BufferedWriter write_buff = new BufferedWriter(write_file);

			for(String str_element : dependencies_list_final)
			{
				write_buff.write(str_element + System.lineSeparator());
			}

			write_buff.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		}
		

		//////////////////////////////////////////////////////////////////////////
		/////// Here we can get the current working directory ///////////////////
		////////////////////////////////////////////////////////////////////////

		Path currentRelativePath = Paths.get("");

		String the_current_address = currentRelativePath.toAbsolutePath().toString();

		/////////////////////////////////////////////////////////////////////////
		////// A collection of string keywords to use for the Docker file //////
        ///////////////////////////////////////////////////////////////////////

		String from_key = "FROM";
		String run_key = "RUN";
		String add_key = "ADD";
		String workdir_key = "WORKDIR";
		String expose_key = "EXPOSE";
		String cmd_key = "CMD";
		String copy_key = "COPY";
		String maintain_key = "MAINTAIN";
		String python_key = "python";
		String server_py = "server.py";
		String entry_point = "ENTRYPOINT";
		String golang_server_key ="golang";
		String server_go =" server.go";


		//////////////////////////////////////////////////////////////////////////
		///////     Now, for some mandatory input from the user     //////////////
		//////////////////////////////////////////////////////////////////////////

		String workdir_final_key = new String();

		workdir_final_key = "./" + workdir_key;

		String maintain_final_key = new String();

		System.out.println("Enter the contributor information :");

		maintain_final_key = scan.nextLine();

		/////////////////////////////////////////////////////////////////////////
		////////      Time to start writing into the Dockerfile     ////////////
		///////////////////////////////////////////////////////////////////////

		File new_dockerfile = new File(master_folder_name + "/Dockerfile");

		if(new_dockerfile.exists())
		{
			new_dockerfile.delete();
		}

		try
		{
			new_dockerfile.createNewFile();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			FileWriter docker_file_writer = new FileWriter(new_dockerfile);

			BufferedWriter docker_writer = new BufferedWriter(docker_file_writer);

			docker_writer.write(from_key + " " + package_installer_info[0] + System.lineSeparator()); // Starting with a very small base image

			docker_writer.write(System.lineSeparator() + maintain_key + " " + maintain_final_key + System.lineSeparator());

			// WE DON'T NEED WORK DIRECTORY BECAUSE THAT'S ALREADY TAKEN CARE OF //

			docker_writer.write(System.lineSeparator() + run_key + " " + package_installer_info[1] + "/requirements.txt" + System.lineSeparator());

			docker_writer.write(System.lineSeparator() + expose_key + " 8080" + System.lineSeparator()); // By default, it's taken as the port 8080, it cant be changed manually later.

			docker_writer.write(System.lineSeparator() + cmd_key + " [\"" + package_installer_info[2] + "\",\"" + package_installer_info[3] + "\"]");

			docker_writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

 		scan.close();
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	///////////          Getting the list of required dependencies          //////////////////
	/////////////////////////////////////////////////////////////////////////////////////////

	public static String[] dependencies_acquire(String[] broken_string/* int no_of_dependencies */, String the_file_extension)
	{
		List<String> dependencies_list = new ArrayList<String>();

		String import_ref = new String();

		String temp_string = new String();

		String from_keyword = new String();

		if(the_file_extension.equals("py"))
		{
			import_ref = "import";

			from_keyword = "from";

			for(int i = 0; i < broken_string.length - 1; i++)
			{
				if(import_ref.equals(broken_string[i]) || from_keyword.equals(broken_string[i]))
				{
					temp_string = broken_string[i + 1];

					dependencies_list.add(temp_string);

					i++;  // No need to read the same keyword again next time
				}
			}
		}

		// Code for python works just fine !!! Check for the GOLANG version.

		// The no of dependencies will be different for GOLANG

		// Need to work on this selection scheme.
		else if(the_file_extension.equals("go"))
		{
			import_ref = "import";

			int k = 0;

			for(int i = 0; i < broken_string.length; i++)
			{
				k = i;

				int post_import_index; 

				if(import_ref.equals(broken_string[k]))
				{
					post_import_index = k + 1;

					while(! broken_string[post_import_index].equals(")"))
					{
						if(broken_string[post_import_index].equals("(") || broken_string[post_import_index].equals("\\s+") || broken_string[post_import_index].equals("\\r?\\n") ||broken_string[post_import_index].equals("\\t"))
						{
							post_import_index++;
						}
						else
						{
							temp_string = broken_string[post_import_index];

							String new_temp_string = temp_string.replaceAll("\"","");

							dependencies_list.add(new_temp_string);

							post_import_index++;
						}

					}
				}
			}
		}

		String[] dependencies_list_final = (String[]) dependencies_list.toArray(new String[dependencies_list.size()]);

		return dependencies_list_final;

		// By using the length method  of the array, we get the no of dependencies
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	//////////////      Getting the extension from the source file      ////////////////////
	///////////////////////////////////////////////////////////////////////////////////////

	public static String get_extension(String file_name)
	{
		String file_extension = file_name.split("\\.")[1];

		return file_extension;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	/////////////         Setting it for different langauges          /////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	public static String[] package_recognizer(String file_extension)
	{
		String[] package_info_array = new String[4];

		// 1. Stores the information about base image
		// 2. Stores the information about the package install
		// Both in string expressions.

		if(file_extension.equals("py"))
		{
			package_info_array[0] = "python:3.6-alpine";

			package_info_array[1] = "pip install -r";

			package_info_array[2] = "python";

			package_info_array[3] = "server.py";
		}
		else if(file_extension.equals("go"))
		{
			package_info_array[0] = "golang:latest";

			package_info_array[1] = "go install";

			package_info_array[2] = "";

			package_info_array[3] = "";
		}
		else
		{
			System.out.println("NOT A SUPPORTED FILE TYPE !!!");

			return null;
		}
		
		return package_info_array;

	}

	///////////////////////////////////////////////////////////////////////////////////
	//////////////    Getting the pre-processing directive    ////////////////////////
	/////////////////////////////////////////////////////////////////////////////////

	public static String getting_preprocess(String file_extension)
	{
		String the_preprocess = new String();

		if(file_extension.equals("py"))
		{
			the_preprocess = "import";
		}
		else if(file_extension.equals("go"))
		{
			the_preprocess = "import";
		}

		return the_preprocess;

	}

	/////////////////////////////////////////////////////////////////////////////////
	////////////      Terminal banner code, just for fun XD     ////////////////////
	///////////////////////////////////////////////////////////////////////////////

	public static void treqs_banner()
	{
		System.out.println();
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println(" ,-------------------,         ,----------,  ,==========,     ,===========,         ____________      ");
		System.out.println(" '------- , ,--------'         |----------|  |=========='    / /---------|'|      //____________||    ");
		System.out.println("          | |                  |  |     | |  | |            | |          | |      | |                 ");
		System.out.println("          | |                  |  |     | |  | |            | |          | |      | |                 ");
		System.out.println("          | |          ,=====, |  |-----' |  |----------,   | |          | |      | |===========..    ");
		System.out.println("          | |          '=====' |  |-------'  |=========='   | |     ,,   | |      |____________, |    ");
		System.out.println("          | |                  |  | | |      | |            | |     | |  | |                   | |    ");        
		System.out.println("          | |                  |  |  | |     | |            | |      | | | |                   | |    ");
		System.out.println("          | |                  |  |   | |    |==========,    | | -------/ /,,,,   ,============, |    ");
		System.out.println("          '='                  '=='    '='   '=========='     '=========----====   |______________/'  ");
		System.out.println();
		System.out.println("-------------------------------------          !!!  WELCOME  !!!        -----------------------------------------");           
	}	

		
}