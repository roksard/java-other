import React from 'react'

const RawData = ({ data }) => {
	return (
		<div>
            <pre>
            {JSON.stringify(data, null, 4)}
            </pre>
		</div>
	)
};

export default RawData