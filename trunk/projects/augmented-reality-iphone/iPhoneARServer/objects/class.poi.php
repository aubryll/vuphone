<?php
/*
	This SQL query will create the table to store your object.

	CREATE TABLE `poi` (
	`poiid` int(11) NOT NULL auto_increment,
	`title` TINYTEXT NOT NULL,
	`description` TEXT NOT NULL,
	`lat` FLOAT NOT NULL,
	`lon` FLOAT NOT NULL,
	`altitude` FLOAT NOT NULL,
	`url` TINYTEXT NOT NULL,
	`quadrant_x` DOUBLE NOT NULL,
	`quadrant_y` DOUBLE NOT NULL, PRIMARY KEY  (`poiid`)) ENGINE=MyISAM;
*/

/**
* <b>POI</b> class with integrated CRUD methods.
* @author Php Object Generator
* @version POG 3.0e / PHP5.1 MYSQL
* @see http://www.phpobjectgenerator.com/plog/tutorials/45/pdo-mysql
* @copyright Free for personal & commercial use. (Offered under the BSD license)
* @link http://www.phpobjectgenerator.com/?language=php5.1&wrapper=pdo&pdoDriver=mysql&objectName=POI&attributeList=array+%28%0A++0+%3D%3E+%27title%27%2C%0A++1+%3D%3E+%27description%27%2C%0A++2+%3D%3E+%27lat%27%2C%0A++3+%3D%3E+%27lon%27%2C%0A++4+%3D%3E+%27altitude%27%2C%0A++5+%3D%3E+%27url%27%2C%0A++6+%3D%3E+%27quadrant_x%27%2C%0A++7+%3D%3E+%27quadrant_y%27%2C%0A%29&typeList=array%2B%2528%250A%2B%2B0%2B%253D%253E%2B%2527TINYTEXT%2527%252C%250A%2B%2B1%2B%253D%253E%2B%2527TEXT%2527%252C%250A%2B%2B2%2B%253D%253E%2B%2527FLOAT%2527%252C%250A%2B%2B3%2B%253D%253E%2B%2527FLOAT%2527%252C%250A%2B%2B4%2B%253D%253E%2B%2527FLOAT%2527%252C%250A%2B%2B5%2B%253D%253E%2B%2527TINYTEXT%2527%252C%250A%2B%2B6%2B%253D%253E%2B%2527DOUBLE%2527%252C%250A%2B%2B7%2B%253D%253E%2B%2527DOUBLE%2527%252C%250A%2529
*/
include_once('class.pog_base.php');
class POI extends POG_Base
{
	public $poiId = '';

	/**
	 * @var TINYTEXT
	 */
	public $title;
	
	/**
	 * @var TEXT
	 */
	public $description;
	
	/**
	 * @var FLOAT
	 */
	public $lat;
	
	/**
	 * @var FLOAT
	 */
	public $lon;
	
	/**
	 * @var FLOAT
	 */
	public $altitude;
	
	/**
	 * @var TINYTEXT
	 */
	public $url;
	
	/**
	 * @var DOUBLE
	 */
	public $quadrant_x;
	
	/**
	 * @var DOUBLE
	 */
	public $quadrant_y;
	
	public $pog_attribute_type = array(
		"poiId" => array('db_attributes' => array("NUMERIC", "INT")),
		"title" => array('db_attributes' => array("TEXT", "TINYTEXT")),
		"description" => array('db_attributes' => array("TEXT", "TEXT")),
		"lat" => array('db_attributes' => array("NUMERIC", "FLOAT")),
		"lon" => array('db_attributes' => array("NUMERIC", "FLOAT")),
		"altitude" => array('db_attributes' => array("NUMERIC", "FLOAT")),
		"url" => array('db_attributes' => array("TEXT", "TINYTEXT")),
		"quadrant_x" => array('db_attributes' => array("NUMERIC", "DOUBLE")),
		"quadrant_y" => array('db_attributes' => array("NUMERIC", "DOUBLE")),
		);
	public $pog_query;
	
	
	/**
	* Getter for some private attributes
	* @return mixed $attribute
	*/
	public function __get($attribute)
	{
		if (isset($this->{"_".$attribute}))
		{
			return $this->{"_".$attribute};
		}
		else
		{
			return false;
		}
	}
	
	function POI($title='', $description='', $lat='', $lon='', $altitude='', $url='', $quadrant_x='', $quadrant_y='')
	{
		$this->title = $title;
		$this->description = $description;
		$this->lat = $lat;
		$this->lon = $lon;
		$this->altitude = $altitude;
		$this->url = $url;
		$this->quadrant_x = $quadrant_x;
		$this->quadrant_y = $quadrant_y;
	}
	
	
	/**
	* Gets object from database
	* @param integer $poiId 
	* @return object $POI
	*/
	function Get($poiId)
	{
		$connection = Database::Connect();
		$this->pog_query = "select * from `poi` where `poiid`='".intval($poiId)."' LIMIT 1";
		$cursor = Database::Reader($this->pog_query, $connection);
		while ($row = Database::Read($cursor))
		{
			$this->poiId = $row['poiid'];
			$this->title = $this->Unescape($row['title']);
			$this->description = $this->Unescape($row['description']);
			$this->lat = $this->Unescape($row['lat']);
			$this->lon = $this->Unescape($row['lon']);
			$this->altitude = $this->Unescape($row['altitude']);
			$this->url = $this->Unescape($row['url']);
			$this->quadrant_x = $this->Unescape($row['quadrant_x']);
			$this->quadrant_y = $this->Unescape($row['quadrant_y']);
		}
		return $this;
	}
	
	
	/**
	* Returns a sorted array of objects that match given conditions
	* @param multidimensional array {("field", "comparator", "value"), ("field", "comparator", "value"), ...} 
	* @param string $sortBy 
	* @param boolean $ascending 
	* @param int limit 
	* @return array $poiList
	*/
	function GetList($fcv_array = array(), $sortBy='', $ascending=true, $limit='')
	{
		$connection = Database::Connect();
		$sqlLimit = ($limit != '' ? "LIMIT $limit" : '');
		$this->pog_query = "select * from `poi` ";
		$poiList = Array();
		if (sizeof($fcv_array) > 0)
		{
			$this->pog_query .= " where ";
			for ($i=0, $c=sizeof($fcv_array); $i<$c; $i++)
			{
				if (sizeof($fcv_array[$i]) == 1)
				{
					$this->pog_query .= " ".$fcv_array[$i][0]." ";
					continue;
				}
				else
				{
					if ($i > 0 && sizeof($fcv_array[$i-1]) != 1)
					{
						$this->pog_query .= " AND ";
					}
					if (isset($this->pog_attribute_type[$fcv_array[$i][0]]['db_attributes']) && $this->pog_attribute_type[$fcv_array[$i][0]]['db_attributes'][0] != 'NUMERIC' && $this->pog_attribute_type[$fcv_array[$i][0]]['db_attributes'][0] != 'SET')
					{
						if ($GLOBALS['configuration']['db_encoding'] == 1)
						{
							$value = POG_Base::IsColumn($fcv_array[$i][2]) ? "BASE64_DECODE(".$fcv_array[$i][2].")" : "'".$fcv_array[$i][2]."'";
							$this->pog_query .= "BASE64_DECODE(`".$fcv_array[$i][0]."`) ".$fcv_array[$i][1]." ".$value;
						}
						else
						{
							$value =  POG_Base::IsColumn($fcv_array[$i][2]) ? $fcv_array[$i][2] : "'".$this->Escape($fcv_array[$i][2])."'";
							$this->pog_query .= "`".$fcv_array[$i][0]."` ".$fcv_array[$i][1]." ".$value;
						}
					}
					else
					{
						$value = POG_Base::IsColumn($fcv_array[$i][2]) ? $fcv_array[$i][2] : "'".$fcv_array[$i][2]."'";
						$this->pog_query .= "`".$fcv_array[$i][0]."` ".$fcv_array[$i][1]." ".$value;
					}
				}
			}
		}
		if ($sortBy != '')
		{
			if (isset($this->pog_attribute_type[$sortBy]['db_attributes']) && $this->pog_attribute_type[$sortBy]['db_attributes'][0] != 'NUMERIC' && $this->pog_attribute_type[$sortBy]['db_attributes'][0] != 'SET')
			{
				if ($GLOBALS['configuration']['db_encoding'] == 1)
				{
					$sortBy = "BASE64_DECODE($sortBy) ";
				}
				else
				{
					$sortBy = "$sortBy ";
				}
			}
			else
			{
				$sortBy = "$sortBy ";
			}
		}
		else
		{
			$sortBy = "poiid";
		}
		$this->pog_query .= " order by ".$sortBy." ".($ascending ? "asc" : "desc")." $sqlLimit";
		$thisObjectName = get_class($this);
		$cursor = Database::Reader($this->pog_query, $connection);
		while ($row = Database::Read($cursor))
		{
			$poi = new $thisObjectName();
			$poi->poiId = $row['poiid'];
			$poi->title = $this->Unescape($row['title']);
			$poi->description = $this->Unescape($row['description']);
			$poi->lat = $this->Unescape($row['lat']);
			$poi->lon = $this->Unescape($row['lon']);
			$poi->altitude = $this->Unescape($row['altitude']);
			$poi->url = $this->Unescape($row['url']);
			$poi->quadrant_x = $this->Unescape($row['quadrant_x']);
			$poi->quadrant_y = $this->Unescape($row['quadrant_y']);
			$poiList[] = $poi;
		}
		return $poiList;
	}
	
	
	/**
	* Saves the object to the database
	* @return integer $poiId
	*/
	function Save()
	{
		$connection = Database::Connect();
		$this->pog_query = "select `poiid` from `poi` where `poiid`='".$this->poiId."' LIMIT 1";
		$rows = Database::Query($this->pog_query, $connection);
		if ($rows > 0)
		{
			$this->pog_query = "update `poi` set 
			`title`='".$this->Escape($this->title)."', 
			`description`='".$this->Escape($this->description)."', 
			`lat`='".$this->Escape($this->lat)."', 
			`lon`='".$this->Escape($this->lon)."', 
			`altitude`='".$this->Escape($this->altitude)."', 
			`url`='".$this->Escape($this->url)."', 
			`quadrant_x`='".$this->Escape($this->quadrant_x)."', 
			`quadrant_y`='".$this->Escape($this->quadrant_y)."' where `poiid`='".$this->poiId."'";
		}
		else
		{
			$this->pog_query = "insert into `poi` (`title`, `description`, `lat`, `lon`, `altitude`, `url`, `quadrant_x`, `quadrant_y` ) values (
			'".$this->Escape($this->title)."', 
			'".$this->Escape($this->description)."', 
			'".$this->Escape($this->lat)."', 
			'".$this->Escape($this->lon)."', 
			'".$this->Escape($this->altitude)."', 
			'".$this->Escape($this->url)."', 
			'".$this->Escape($this->quadrant_x)."', 
			'".$this->Escape($this->quadrant_y)."' )";
		}
		$insertId = Database::InsertOrUpdate($this->pog_query, $connection);
		if ($this->poiId == "")
		{
			$this->poiId = $insertId;
		}
		return $this->poiId;
	}
	
	
	/**
	* Clones the object and saves it to the database
	* @return integer $poiId
	*/
	function SaveNew()
	{
		$this->poiId = '';
		return $this->Save();
	}
	
	
	/**
	* Deletes the object from the database
	* @return boolean
	*/
	function Delete()
	{
		$connection = Database::Connect();
		$this->pog_query = "delete from `poi` where `poiid`='".$this->poiId."'";
		return Database::NonQuery($this->pog_query, $connection);
	}
	
	
	/**
	* Deletes a list of objects that match given conditions
	* @param multidimensional array {("field", "comparator", "value"), ("field", "comparator", "value"), ...} 
	* @param bool $deep 
	* @return 
	*/
	function DeleteList($fcv_array)
	{
		if (sizeof($fcv_array) > 0)
		{
			$connection = Database::Connect();
			$pog_query = "delete from `poi` where ";
			for ($i=0, $c=sizeof($fcv_array); $i<$c; $i++)
			{
				if (sizeof($fcv_array[$i]) == 1)
				{
					$pog_query .= " ".$fcv_array[$i][0]." ";
					continue;
				}
				else
				{
					if ($i > 0 && sizeof($fcv_array[$i-1]) !== 1)
					{
						$pog_query .= " AND ";
					}
					if (isset($this->pog_attribute_type[$fcv_array[$i][0]]['db_attributes']) && $this->pog_attribute_type[$fcv_array[$i][0]]['db_attributes'][0] != 'NUMERIC' && $this->pog_attribute_type[$fcv_array[$i][0]]['db_attributes'][0] != 'SET')
					{
						$pog_query .= "`".$fcv_array[$i][0]."` ".$fcv_array[$i][1]." '".$this->Escape($fcv_array[$i][2])."'";
					}
					else
					{
						$pog_query .= "`".$fcv_array[$i][0]."` ".$fcv_array[$i][1]." '".$fcv_array[$i][2]."'";
					}
				}
			}
			return Database::NonQuery($pog_query, $connection);
		}
	}
}
?>